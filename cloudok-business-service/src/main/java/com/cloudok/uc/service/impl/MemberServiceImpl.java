package com.cloudok.uc.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.cloudok.base.message.utils.MessageUtil;
import com.cloudok.base.message.vo.MessageReceive;
import com.cloudok.cache.Cache;
import com.cloudok.common.CacheType;
import com.cloudok.common.Constants;
import com.cloudok.core.context.SpringApplicationContext;
import com.cloudok.core.convert.Convert;
import com.cloudok.core.enums.UserType;
import com.cloudok.core.exception.CoreExceptionMessage;
import com.cloudok.core.exception.SystemException;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.query.QueryOperator;
import com.cloudok.core.service.AbstractService;
import com.cloudok.core.vo.Page;
import com.cloudok.exception.CloudOKExceptionMessage;
import com.cloudok.security.SecurityContextHelper;
import com.cloudok.security.User;
import com.cloudok.security.UserInfoHandler;
import com.cloudok.security.exception.SecurityExceptionMessage;
import com.cloudok.security.token.JWTTokenInfo;
import com.cloudok.security.token.JWTUtil;
import com.cloudok.security.token.TokenType;
import com.cloudok.uc.dto.SimpleMemberDTO;
import com.cloudok.uc.dto.SimpleMemberInfo;
import com.cloudok.uc.dto.WholeMemberDTO;
import com.cloudok.uc.event.MemberCreateEvent;
import com.cloudok.uc.event.MemberUpdateEvent;
import com.cloudok.uc.mapper.MemberMapper;
import com.cloudok.uc.mapper.MemberTagsMapper;
import com.cloudok.uc.mapping.EducationExperienceMapping;
import com.cloudok.uc.mapping.InternshipExperienceMapping;
import com.cloudok.uc.mapping.MemberMapping;
import com.cloudok.uc.mapping.MemberTagsMapping;
import com.cloudok.uc.mapping.ProjectExperienceMapping;
import com.cloudok.uc.mapping.RecognizedMapping;
import com.cloudok.uc.mapping.ResearchExperienceMapping;
import com.cloudok.uc.po.LinkMemberPO;
import com.cloudok.uc.po.MemberPO;
import com.cloudok.uc.po.MemberTagsPO;
import com.cloudok.uc.service.EducationExperienceService;
import com.cloudok.uc.service.InternshipExperienceService;
import com.cloudok.uc.service.MemberService;
import com.cloudok.uc.service.MemberTagsService;
import com.cloudok.uc.service.ProjectExperienceService;
import com.cloudok.uc.service.RecognizedService;
import com.cloudok.uc.service.ResearchExperienceService;
import com.cloudok.uc.vo.BindRequest;
import com.cloudok.uc.vo.ChangePasswordRequest;
import com.cloudok.uc.vo.EducationExperienceVO;
import com.cloudok.uc.vo.ForgotVO;
import com.cloudok.uc.vo.InternshipExperienceVO;
import com.cloudok.uc.vo.LinkMemberVO;
import com.cloudok.uc.vo.LoginVO;
import com.cloudok.uc.vo.MemberTagsVO;
import com.cloudok.uc.vo.MemberVO;
import com.cloudok.uc.vo.MemberVO.UserState;
import com.cloudok.uc.vo.ProjectExperienceVO;
import com.cloudok.uc.vo.RecognizedVO;
import com.cloudok.uc.vo.ResearchExperienceVO;
import com.cloudok.uc.vo.SingupVO;
import com.cloudok.uc.vo.TokenVO;
import com.cloudok.uc.vo.UserCheckRequest;
import com.cloudok.uc.vo.VerifyCodeRequest;

//@Slf4j
@Service
public class MemberServiceImpl extends AbstractService<MemberVO, MemberPO> implements MemberService, UserInfoHandler {

	@Component
	public static class MemberConvert implements Convert<MemberVO, MemberPO> {

		@Override
		public MemberPO convert2PO(MemberVO d) {
			MemberPO po = new MemberPO();
			BeanUtils.copyProperties(d, po);
			if (d.getState() != null) {
				long state = 0;
				if (d.getState().isCheckEmail()) {
					state = state | (1 << 2);
				}
				if (d.getState().isFillEduInfo()) {
					state = state | (1 << 1);
				}
				if (d.getState().isFillUserInfo()) {
					state = state | 1;
				}
				po.setState(state);
			}
			return po;
		}

		@Override
		public List<MemberPO> convert2PO(List<MemberVO> d) {
			List<MemberPO> es = new ArrayList<MemberPO>();
			if (d != null && d.size() > 0) {
				d.forEach(item -> es.add(convert2PO(item)));
			}
			return es;
		}

		@Override
		public MemberVO convert2VO(MemberPO e) {
			MemberVO vo = new MemberVO();
			BeanUtils.copyProperties(e, vo);
			if (e.getState() != null) {
				vo.setState(UserState.build(e.getState()));
			}
			return vo;
		}

		@Override
		public List<MemberVO> convert2VO(List<MemberPO> e) {
			List<MemberVO> es = new ArrayList<MemberVO>();
			if (e != null && e.size() > 0) {
				e.forEach(item -> es.add(convert2VO(item)));
			}
			return es;
		}

	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private Cache cacheService;

	private MemberMapper repository;

	@Autowired
	public MemberServiceImpl(MemberMapper repository, MemberConvert convert) {
		super(repository, convert);
		this.repository = repository;
	}

	@Override
	public TokenVO login(LoginVO vo) {
		List<MemberVO> memberList = this
				.list(QueryBuilder.create(MemberMapping.class).and(MemberMapping.EMAIL, vo.getUserName()).end() // email
						.or(MemberMapping.PHONE, vo.getUserName()) // phone
						.or(MemberMapping.USERNAME, vo.getUserName()) // userName
						.end());

		MemberVO sysUser = null;
		if (!StringUtils.isEmpty(vo.getCode())) {
			boolean isSms = "0".equalsIgnoreCase(vo.getLoginType());
			if(!"888888".equals(vo.getCode())) {// test code
				String cacheKey = buildKey("login", isSms ? "sms" : "email", vo.getUserName());
				String code = cacheService.get(CacheType.VerifyCode, cacheKey, String.class);
				if (StringUtils.isEmpty(code)) {
					throw new SystemException("verify code is wrong", CloudOKExceptionMessage.VERIFY_CODE_WRONG);
				}
				if (!code.equals(vo.getCode())) {
					throw new SystemException("verify code is wrong", CloudOKExceptionMessage.VERIFY_CODE_WRONG);
				}
			}
			// 如果通过验证码登录，且用户不存在时直接创建用户
			if (CollectionUtils.isEmpty(memberList)) {
				MemberVO member = new MemberVO();
				if ("0".equalsIgnoreCase(vo.getLoginType())) {
					member.setPhone(vo.getUserName());
				} else {
					member.setEmail(vo.getUserName());
				}
				this.create(member);
				sysUser = this.get(member.getId());
			} else {
				sysUser = memberList.get(0);
			}
		} else {
			sysUser = memberList.get(0);
			if (CollectionUtils.isEmpty(memberList)) {
				throw new SystemException(SecurityExceptionMessage.ACCESS_INCORRECT_CERTIFICATE);
			}

			if (!passwordEncoder.matches(vo.getPassword(), sysUser.getPassword())) {
				throw new SystemException(SecurityExceptionMessage.ACCESS_INCORRECT_CERTIFICATE);
			}
		}
//		if (sysUser.getFreeze().equals(Boolean.TRUE)) {
//			throw new SystemException(SecurityExceptionMessage.ACCESS_ACCOUNT_FROZEN);
//		}
		cacheService.evict(CacheType.Member, sysUser.getId().toString());
		User user = this.loadUserInfo(sysUser.getId(), sysUser);
		TokenVO token = TokenVO.build(JWTUtil.genToken(user, TokenType.ACCESS),
				JWTUtil.genToken(user, TokenType.REFRESH), user);
		return token;
	}

	private User loadUserInfo(Long userId, MemberVO sysUser) {
		User user = cacheService.get(CacheType.Member, userId.toString(), User.class);
		if (user != null) {
			return user;
		}
		user = new User();
		BeanUtils.copyProperties(sysUser, user);
		user.setUsername(sysUser.getUserName());
		user.setFullName(sysUser.getNickName());
		user.setUserType(UserType.MEMBER.getType());

		user.setAttributes(sysUser.getState());

		fillAuthorities(user);
		cacheService.put(CacheType.Member, userId.toString(), user);
		return user;
	}

	private void fillAuthorities(User user) {
		List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(Constants.DEFAULT_MEMBER_ROLE));
		user.setAuthorities(authorities);
	}

	@Override
	public Boolean logout() {
		User user = SecurityContextHelper.getCurrentUser();
		cacheService.evict(CacheType.Member, user.getId().toString());
		return true;
	}

	@Override
	public TokenVO refreshToken(String refreshToken) {
		JWTTokenInfo info = JWTUtil.decodeToken(refreshToken);
		if (!(info.getTokenType().getType() == TokenType.REFRESH.getType())) {
			throw new SystemException(SecurityExceptionMessage.BAD_CERTIFICATE);
		}
		TokenVO token = TokenVO.build(JWTUtil.genToken(SecurityContextHelper.getCurrentUser(), TokenType.ACCESS),
				JWTUtil.genToken(SecurityContextHelper.getCurrentUser(), TokenType.REFRESH),
				SecurityContextHelper.getCurrentUser());
		return token;
	}

	@Override
	public MemberVO getCurrentUserInfo() {
		MemberVO userVO = this.get(SecurityContextHelper.getCurrentUserId());
		return userVO;
	}

	@Override
	public Boolean resetPwd(ForgotVO vo) {
		boolean isSms = "0".equalsIgnoreCase(vo.getForgotType());
		if (isSms) {
			vo.setEmail(null);
		} else {
			vo.setPhone(null);
		}
		String cacheKey = buildKey("forgot", isSms ? "sms" : "email", isSms ? vo.getPhone() : vo.getEmail());
		String code = cacheService.get(CacheType.VerifyCode, cacheKey, String.class);
		if (StringUtils.isEmpty(code)) {
			throw new SystemException("verify code is wrong", CloudOKExceptionMessage.VERIFY_CODE_WRONG);
		}
		if (!code.equals(vo.getCode())) {
			throw new SystemException("verify code is wrong", CloudOKExceptionMessage.VERIFY_CODE_WRONG);
		}

		List<MemberVO> userList = null;
		if ("1".equalsIgnoreCase(vo.getForgotType())) {
			userList = this
					.list(QueryBuilder.create(MemberMapping.class).and(MemberMapping.EMAIL, vo.getEmail()).end());
			if (CollectionUtils.isEmpty(userList)) {
				throw new SystemException("email not fonud", CoreExceptionMessage.NOTFOUND_ERR);
			}
		} else if ("0".equalsIgnoreCase(vo.getForgotType())) {
			userList = this
					.list(QueryBuilder.create(MemberMapping.class).and(MemberMapping.PHONE, vo.getPhone()).end());
			if (CollectionUtils.isEmpty(userList)) {
				throw new SystemException("phone not found", CoreExceptionMessage.NOTFOUND_ERR);
			}
		}
		MemberVO user = userList.get(0);
		user.setPassword(passwordEncoder.encode(vo.getPassword()));
		this.merge(user);

		return true;
	}

	private String buildKey(String module, String type, String key) {
		return module + ":" + type + ":" + key;
	}

	private boolean isEduEmail(String email) {
		if (StringUtils.isEmpty(email)) {
			return false;
		}
		return email.toLowerCase().endsWith("edu");
	}

	@Override
	public TokenVO signup(SingupVO vo) {
		boolean isSms = "0".equalsIgnoreCase(vo.getRegisterType());
		if (StringUtils.isEmpty(vo.getPhone()) && !StringUtils.isEmpty(vo.getEmail())) { // fixed data, front-end may be
																							// error
			if (!isEduEmail(vo.getEmail())) {
				throw new SystemException(CloudOKExceptionMessage.INVALID_EMAIL_ADDRESS);
			}
			vo.setRegisterType("1"); // force set to email
			isSms = false;
			boolean isExits = checkEmailExists(vo.getEmail());
			if (isExits) {
				throw new SystemException(CloudOKExceptionMessage.EMAIL_ALREADY_EXISTS);
			}
		}
		if (!StringUtils.isEmpty(vo.getPhone()) && StringUtils.isEmpty(vo.getEmail())) { // fixed data, front-end may be
																							// error
			vo.setRegisterType("0"); // force set to phone
			isSms = true;
			boolean isExits = checkPhoneExists(vo.getPhone());
			if (isExits) {
				throw new SystemException(CloudOKExceptionMessage.PHONE_ALREADY_EXISTS);
			}
		}
		if (isSms) {
			vo.setEmail(null);
		} else {
			vo.setPhone(null);
		}
		String cacheKey = buildKey("register", isSms ? "sms" : "email", isSms ? vo.getPhone() : vo.getEmail());
		String code = cacheService.get(CacheType.VerifyCode, cacheKey, String.class);
		if (StringUtils.isEmpty(code)) {
			throw new SystemException("verify code is wrong", CloudOKExceptionMessage.VERIFY_CODE_WRONG);
		}
		if (!code.equals(vo.getCode())) {
			throw new SystemException("verify code is wrong", CloudOKExceptionMessage.VERIFY_CODE_WRONG);
		}

		MemberVO member = new MemberVO();
		member.setEmail(vo.getEmail());
		member.setPhone(vo.getPhone());
//		member.setUserName(StringUtils.isEmpty(vo.getEmail())?vo.getPhone():vo.getEmail());
		this.create(member);

		MemberVO sysUser = this.get(member.getId());
		cacheService.evict(CacheType.Member, sysUser.getId().toString());
		User user = this.loadUserInfo(sysUser.getId(), sysUser);
		TokenVO token = TokenVO.build(JWTUtil.genToken(user, TokenType.ACCESS),
				JWTUtil.genToken(user, TokenType.REFRESH), user);

		SpringApplicationContext.publishEvent(new MemberCreateEvent(member));

		return token;
	}

	@Override
	public MemberVO fillAccountInfo(@Valid MemberVO vo) {
		MemberVO member = new MemberVO();
		boolean isExits = checkUserNameExists(vo.getUserName(), SecurityContextHelper.getCurrentUserId());
		if (isExits) {
			throw new SystemException(CloudOKExceptionMessage.USERNAME_ALREADY_EXISTS);
		}
		member.setId(SecurityContextHelper.getCurrentUserId());
		member.setNickName(vo.getNickName());
		member.setAvatar(vo.getAvatar());
		member.setUserName(vo.getUserName());
		member.setRemark(vo.getRemark());
		member.setBirthDate(vo.getBirthDate());
		member.setState(this.get(SecurityContextHelper.getCurrentUserId()).getState());
		if (member.getState() == null) {
			member.setState(new UserState());
		}
		member.getState().setFillUserInfo(true);
		member.setSex(vo.getSex());
		if (!StringUtils.isEmpty(vo.getPassword())) {
			member.setPassword(passwordEncoder.encode(vo.getPassword()));
		}
		this.merge(member);

		SpringApplicationContext.publishEvent(new MemberUpdateEvent(member));

		return this.get(member.getId());
	}

	@Override
	public Boolean sendVerifycode(VerifyCodeRequest vo) {
		String cacheKey = buildKey(vo.getModule(), vo.getType(), vo.getKey());
		if (cacheService.exist(CacheType.VerifyCode, "S:" + cacheKey)) { // 防止重复发送
			return null;
		}
		Integer count = cacheService.get(CacheType.VerifyCodeCount, cacheKey, Integer.class);
		int _c = count == null ? 0 : count;
		if (_c > 10) { // 每小时最多10次
			throw new SystemException(CloudOKExceptionMessage.TOO_MANY_VERIFY_CODE);
		}

		String code = RandomStringUtils.random(6, false, true);
		int checkType = 0;
		switch (vo.getModule().toLowerCase()) {
		case "register":
			// check unique
			checkType = 1;
			break;
		case "forgot":
			// check exists
			checkType = 2;
			break;
		case "login":
			// check exists
			checkType = 3;
			break;
		case "bind":
			// check unique
			checkType = 1;
			break;
		default:
			throw new SystemException(CloudOKExceptionMessage.UNKNOW_VERIFY_CODE_TYPE);
		}
		if ("sms".equalsIgnoreCase(vo.getType())) {
			boolean exists = this.checkPhoneExists(vo.getKey());
			if (checkType == 1 && exists) {
				if (vo.getModule().toLowerCase().equals("register")) {
					throw new SystemException(CloudOKExceptionMessage.PHONE_ALREADY_EXISTS);
				}
				if (vo.getModule().toLowerCase().equals("bind")) {
					throw new SystemException("Phone number is bound", CloudOKExceptionMessage.PHONE_ALREADY_EXISTS);
				}
			}
			if (checkType == 2 && !exists) {
				throw new SystemException(CloudOKExceptionMessage.PHONE_NOT_FOUND);
			}
		} else if ("email".equalsIgnoreCase(vo.getType())) {
			if (!isEduEmail(vo.getKey())) {
				throw new SystemException(CloudOKExceptionMessage.INVALID_EMAIL_ADDRESS);
			}
			boolean exists = this.checkEmailExists(vo.getKey());
			if (checkType == 1 && exists) {
				if (vo.getModule().toLowerCase().equals("register")) {
					throw new SystemException(CloudOKExceptionMessage.EMAIL_ALREADY_EXISTS);
				}
				if (vo.getModule().toLowerCase().equals("bind")) {
					throw new SystemException("Email is bound", CloudOKExceptionMessage.EMAIL_ALREADY_EXISTS);
				}
			}
			if (checkType == 2 && !exists) {
				throw new SystemException(CloudOKExceptionMessage.EMAIL_NOT_FOUND);
			}
		} else {
			throw new SystemException("type should be email or sms", CloudOKExceptionMessage.VERIFY_CODE_SEND_ERROR);
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("code", code);
		MessageUtil.send(vo.getModule(), params, new MessageReceive.DirectMessageReceive(vo.getKey()));

		// code 10 minutes
		cacheService.put(CacheType.VerifyCode, cacheKey, code, 12, TimeUnit.MINUTES);
		cacheService.put(CacheType.VerifyCode, "S:" + cacheKey, code, 30, TimeUnit.SECONDS);
		cacheService.put(CacheType.VerifyCodeCount, cacheKey, _c + 1, 1, TimeUnit.HOURS);
		return true;
	}

	private boolean checkEmailExists(String email) {
		if (!isEduEmail(email)) {
			throw new SystemException(CloudOKExceptionMessage.INVALID_EMAIL_ADDRESS);
		}
		return !CollectionUtils
				.isEmpty(this.list(QueryBuilder.create(MemberMapping.class).and(MemberMapping.EMAIL, email).end()));
	}

	private boolean checkPhoneExists(String phone) {
		return !CollectionUtils
				.isEmpty(this.list(QueryBuilder.create(MemberMapping.class).and(MemberMapping.PHONE, phone).end()));
	}

	private boolean checkUserNameExists(String userName, Long userId) {
		return this.count(QueryBuilder.create(MemberMapping.class).and(MemberMapping.USERNAME, userName)
				.and(MemberMapping.ID, QueryOperator.NEQ, userId).end()) != 0;
	}

	private boolean checkUserNameExists(String userName) {
		return this.count(QueryBuilder.create(MemberMapping.class).and(MemberMapping.USERNAME, userName).end()) != 0;
	}

	@Override
	public Boolean checkEmail(UserCheckRequest request) {
		return this.checkEmailExists(request.getEmail());
	}

	@Override
	public Boolean checkUserName(UserCheckRequest request) {
		return this.checkUserNameExists(request.getUserName());
	}

	@Override
	public Boolean changePassword(ChangePasswordRequest request) {
		MemberVO currentUser = this.get(SecurityContextHelper.getCurrentUserId());
		if (!passwordEncoder.matches(request.getOldPassword(), currentUser.getPassword())) {
			throw new SystemException("Current password is wrong, please retry", CloudOKExceptionMessage.DEFAULT_ERROR);
		}
		MemberVO me = new MemberVO();
		me.setId(currentUser.getId());
		me.setPassword(passwordEncoder.encode(request.getPassword()));
		this.merge(me);
		return true;
	}

	@Override
	public Boolean bind(BindRequest request) {
		MemberVO vo = this.get(SecurityContextHelper.getCurrentUserId());
		String cacheKey = buildKey("bind", request.getType(), request.getKey());
		String code = cacheService.get(CacheType.VerifyCode, cacheKey, String.class);
		if (StringUtils.isEmpty(code)) {
			throw new SystemException("verify code is wrong", CloudOKExceptionMessage.VERIFY_CODE_WRONG);
		}
		if (!code.equals(request.getCode())) {
			throw new SystemException("verify code is wrong", CloudOKExceptionMessage.VERIFY_CODE_WRONG);
		}
		if ("email".equalsIgnoreCase(request.getType())) {
			MemberVO user = new MemberVO();
			user.setEmail(request.getKey());
			user.setId(vo.getId());
			this.merge(user);
		} else if ("sms".equalsIgnoreCase(request.getType())) {
			MemberVO user = new MemberVO();
			user.setPhone(request.getKey());
			user.setId(vo.getId());
			this.merge(user);
		}
		SpringApplicationContext.publishEvent(new MemberUpdateEvent(vo));
		return true;
	}

	@Override
	public User loadUserInfoByToken(JWTTokenInfo info) {
		return getSessionFromCache(info.getKey());
	}

	private User getSessionFromCache(String key) {
		return cacheService.get(CacheType.Member, key, User.class);
	}

	@Override
	public UserType getUserType() {
		return UserType.MEMBER;
	}

	@Autowired
	private EducationExperienceService educationExperienceService;
	@Autowired
	private InternshipExperienceService internshipExperienceService;
	@Autowired
	private MemberTagsService memberTagsService;
	@Autowired
	private ProjectExperienceService projectExperienceService;
	@Autowired
	private RecognizedService recognizedService;
	@Autowired
	private ResearchExperienceService researchExperienceService;

	@Override
	public List<WholeMemberDTO> getWholeMemberInfo(List<Long> memberIdList) {
		return getWholeMemberInfo(this.get(memberIdList), false);
	}

	@Override
	public List<WholeMemberDTO> getWholeMemberInfo(List<? extends MemberVO> memberVo, boolean ignoreRecognized) {
		List<Long> memberIdList = memberVo.stream().map(MemberVO::getId).distinct().collect(Collectors.toList());
		List<WholeMemberDTO> memberList = memberVo.stream().map(item -> {
			WholeMemberDTO dto = new WholeMemberDTO();
			BeanUtils.copyProperties(item, dto);
			return dto;
		}).collect(Collectors.toList());

		if (!CollectionUtils.isEmpty(memberList)) {
			educationExperienceService
					.list(QueryBuilder.create(EducationExperienceMapping.class)
							.and(EducationExperienceMapping.MEMBERID, QueryOperator.IN, memberIdList).end())
					.stream().collect(Collectors.groupingBy(EducationExperienceVO::getMemberId))
					.forEach((memberId, valueList) -> {
						memberList.stream().filter(item -> item.getId().equals(memberId)).findAny().ifPresent(item -> {
							item.setEducationList(valueList);
						});
					});

			internshipExperienceService
					.list(QueryBuilder.create(EducationExperienceMapping.class)
							.and(InternshipExperienceMapping.MEMBERID, QueryOperator.IN, memberIdList).end())
					.stream().collect(Collectors.groupingBy(InternshipExperienceVO::getMemberId))
					.forEach((memberId, valueList) -> {
						memberList.stream().filter(item -> item.getId().equals(memberId)).findAny().ifPresent(item -> {
							item.setInternshipList(valueList);
						});
					});

			memberTagsService
					.list(QueryBuilder.create(EducationExperienceMapping.class)
							.and(MemberTagsMapping.MEMBERID, QueryOperator.IN, memberIdList).end())
					.stream().collect(Collectors.groupingBy(MemberTagsVO::getMemberId))
					.forEach((memberId, valueList) -> {
						memberList.stream().filter(item -> item.getId().equals(memberId)).findAny().ifPresent(item -> {
							item.setTagsList(valueList);
						});
					});

			projectExperienceService
					.list(QueryBuilder.create(EducationExperienceMapping.class)
							.and(ProjectExperienceMapping.MEMBERID, QueryOperator.IN, memberIdList).end())
					.stream().collect(Collectors.groupingBy(ProjectExperienceVO::getMemberId))
					.forEach((memberId, valueList) -> {
						memberList.stream().filter(item -> item.getId().equals(memberId)).findAny().ifPresent(item -> {
							item.setProjectList(valueList);
						});
					});

			researchExperienceService
					.list(QueryBuilder.create(EducationExperienceMapping.class)
							.and(ResearchExperienceMapping.MEMBERID, QueryOperator.IN, memberIdList).end())
					.stream().collect(Collectors.groupingBy(ResearchExperienceVO::getMemberId))
					.forEach((memberId, valueList) -> {
						memberList.stream().filter(item -> item.getId().equals(memberId)).findAny().ifPresent(item -> {
							item.setResearchList(valueList);
						});
					});
			if (!ignoreRecognized) {
				recognizedService
						.list(QueryBuilder.create(EducationExperienceMapping.class)
								.and(RecognizedMapping.SOURCEID, QueryOperator.IN, memberIdList).end())
						.stream().collect(Collectors.groupingBy(RecognizedVO::getSourceId))
						.forEach((memberId, valueList) -> {
							memberList.stream().filter(item -> item.getId().equals(memberId)).findAny()
									.ifPresent(item -> {
										item.setRecognizedMemberList(valueList.stream().map(i -> i.getTargetId())
												.distinct().collect(Collectors.toList()));
									});
						});

				recognizedService
						.list(QueryBuilder.create(EducationExperienceMapping.class)
								.and(RecognizedMapping.TARGETID, QueryOperator.IN, memberIdList).end())
						.stream().collect(Collectors.groupingBy(RecognizedVO::getSourceId))
						.forEach((memberId, valueList) -> {
							memberList.stream().filter(item -> item.getId().equals(memberId)).findAny()
									.ifPresent(item -> {
										item.setRecognizedByList(valueList.stream().map(i -> i.getSourceId()).distinct()
												.collect(Collectors.toList()));
									});
						});
			}

		}
		return memberList;
	}

	@Override
	public WholeMemberDTO getWholeMemberInfo(Long memberId) {
		List<WholeMemberDTO> list = this.getWholeMemberInfo(java.util.Collections.singletonList(memberId));
		return CollectionUtils.isEmpty(list) ? null : list.get(0);
	}

//	private void syncToMongoDB(Long memberId) {
//		//构建完整对象
//		WholeMemberDTO member = this.getWholeMemberInfo(memberId);
//		//查询数据库数据
//		Query query=new Query(Criteria.where("id").is(memberId));
//		Document doc = new Document(); // org.bson.Document
//		mongoTemplate.getConverter().write(member, doc);
//		Update update = Update.fromDocument(doc);
//		UpdateResult result = mongoTemplate.upsert(query, update, Constants.MONGODB_MEMBER_COLLECTION_NAME);
//		log.info("sync member[{}] info to mongodb,effect rows={}", memberId, result.getModifiedCount());
//	}
//	
//	private void addToMongoDB(Long memberId) {
//		//构建完整对象
//		WholeMemberDTO member = this.getWholeMemberInfo(memberId);
//		//查询数据库数据
//		Document doc = new Document(); // org.bson.Document
//		mongoTemplate.getConverter().write(member, doc);
//		mongoTemplate.save(doc, Constants.MONGODB_MEMBER_COLLECTION_NAME);
//		log.info("add member[{}] info to mongodb,effect rows={}", memberId);
//	}

	@Override
	public Boolean checkPhone(UserCheckRequest request) {
		return !CollectionUtils.isEmpty(
				this.list(QueryBuilder.create(MemberMapping.class).and(MemberMapping.PHONE, request.getPhone()).end()));
	}

	@Override
	public Page<WholeMemberDTO> link(QueryBuilder builder) {
		builder.addParameter("memberId", getCurrentUserId()).addParameter("ignore", true);
		Page<LinkMemberVO> page = new Page<>();
		page.setTotalCount(repository.countQueryLinkMember(builder.excludeSortPage()));
		page.setPageNo(builder.getPageCondition().getPageNo());
		page.setPageSize(builder.getPageCondition().getPageSize());
		if (page.getTotalCount() > 0 && (page.getTotalCount() / builder.getPageCondition().getPageSize() + 1) >= builder
				.getPageCondition().getPageNo()) {
			List<LinkMemberPO> es = repository.queryLinkMember(builder);
			if (es != null) {
				page.setData(es.stream().map(item -> {
					LinkMemberVO vo = new LinkMemberVO();
					BeanUtils.copyProperties(item, vo);
					vo.setState(UserState.build(item.getState()));
					return vo;
				}).collect(Collectors.toList()));
			}
		}

		Page<WholeMemberDTO> result = new Page<WholeMemberDTO>();
		result.setPageNo(page.getPageNo());
		result.setPageSize(page.getPageSize());
		result.setTotalCount(page.getTotalCount());
		if (!CollectionUtils.isEmpty(page.getData())) {
			result.setData(getWholeMemberInfo(page.getData(), true));
		}
		return result;
	}

	@Override
	public WholeMemberDTO link(Long id) {
		LinkMemberPO po = repository
				.queryLinkMember(QueryBuilder.create(MemberMapping.class).addParameter("memberId", getCurrentUserId())
						.addParameter("ignore", false).and(MemberMapping.ID, id).end())
				.get(0);
		LinkMemberVO vo = new LinkMemberVO();
		BeanUtils.copyProperties(po, vo);
		vo.setState(UserState.build(po.getState()));
		return getWholeMemberInfo(Collections.singletonList(vo), true).get(0);
	}

	@Override
	public List<SimpleMemberInfo> getSimpleMemberInfo(List<Long> memberIdList) {
		memberIdList = memberIdList.stream().distinct().collect(Collectors.toList());
		List<SimpleMemberInfo> memberList = this.get(memberIdList).stream().map(item -> {
			SimpleMemberInfo dto = new SimpleMemberInfo();
			BeanUtils.copyProperties(item, dto);
			return dto;
		}).collect(Collectors.toList());

		if (!CollectionUtils.isEmpty(memberList)) {
			educationExperienceService
					.list(QueryBuilder.create(EducationExperienceMapping.class)
							.and(EducationExperienceMapping.MEMBERID, QueryOperator.IN, memberIdList).end()
							.sort(EducationExperienceMapping.GRADE).desc())
					.stream().collect(Collectors.groupingBy(EducationExperienceVO::getMemberId))
					.forEach((memberId, valueList) -> {
						memberList.stream().filter(item -> item.getId().equals(memberId)).findAny().ifPresent(item -> {
							item.setEducation(valueList.get(0));
						});
					});
		}
		return memberList;
	}

	@Override
	public SimpleMemberDTO getSimpleMemberInfo() {
		List<EducationExperienceVO> edu = educationExperienceService.list(QueryBuilder
				.create(EducationExperienceMapping.class).and(EducationExperienceMapping.MEMBERID, getCurrentUserId())
				.end().sort(EducationExperienceMapping.GRADE).desc().enablePaging().pageNo(1).pageSize(1).end());
		return SimpleMemberDTO.builder().member(this.get(getCurrentUserId()))
				.eduExperience(CollectionUtils.isEmpty(edu) ? null : edu.get(0))
				.friendCount(recognizedService.getFriendCount())
				.fromCount(recognizedService.count(QueryBuilder.create(RecognizedMapping.class).and(RecognizedMapping.TARGETID, getCurrentUserId()).end()))
				.toCount(recognizedService.count(QueryBuilder.create(RecognizedMapping.class).and(RecognizedMapping.SOURCEID, getCurrentUserId()).end()))
				.newFrom(recognizedService.count(QueryBuilder.create(RecognizedMapping.class).and(RecognizedMapping.TARGETID, getCurrentUserId()).and(RecognizedMapping.READ, false).end()))
				.build();
	}
	

	@Autowired
	private MemberTagsMapper memberTagsMapper;
	
	@Override
	public IdenticalCountVO identical(Long id) {
		IdenticalCountVO vo=new IdenticalCountVO();
		List<LinkMemberPO> linkMemberPOs = repository.queryFriends(QueryBuilder.create(MemberMapping.class).addParameter("memberId", id));
		if(!CollectionUtils.isEmpty(linkMemberPOs)) {
			List<LinkMemberPO> linkMemberPOs1 = repository.queryFriends(QueryBuilder.create(MemberMapping.class).addParameter("memberId", getCurrentUserId()));
			if(!CollectionUtils.isEmpty(linkMemberPOs1)) {
				vo.setFriends((int)linkMemberPOs.stream().filter(item->linkMemberPOs1.stream().filter(item1->item1.getId().equals(item.getId())).findAny().isPresent()).count());
			}
		}
		List<MemberTagsPO> mtags1 = memberTagsMapper.select(QueryBuilder.create(MemberTagsMapping.class).and(MemberTagsMapping.MEMBERID, id).end());
		if(!CollectionUtils.isEmpty(mtags1)) {
			List<MemberTagsPO> mtags2 = memberTagsMapper.select(QueryBuilder.create(MemberTagsMapping.class).and(MemberTagsMapping.MEMBERID, getCurrentUserId()).end());
			if(!CollectionUtils.isEmpty(mtags2)) {
				vo.setTags((int)mtags1.stream().filter(item->mtags1.stream().filter(item1->item1.getTagId().equals(item.getTagId())).findAny().isPresent()).count());
			}
		}
		return vo;
	}

	@Override
	public Page<WholeMemberDTO> friend(String type, QueryBuilder builder) {
		builder.addParameter("memberId", getCurrentUserId());
		builder.addParameter("type", type);
		Page<WholeMemberDTO> page = new Page<>();
		page.setTotalCount(repository.friendCount(builder.excludeSortPage()));
		page.setPageNo(builder.getPageCondition().getPageNo());
		page.setPageSize(builder.getPageCondition().getPageSize());
		if (page.getTotalCount() > 0 && (page.getTotalCount() / builder.getPageCondition().getPageSize() + 1) >= builder
				.getPageCondition().getPageNo()) {
			List<MemberPO> es = repository.friend(builder);
			if (es != null) {
				page.setData(es.stream().map(item -> {
					WholeMemberDTO vo = new WholeMemberDTO();
					BeanUtils.copyProperties(item, vo);
					return vo;
				}).collect(Collectors.toList()));
			}
		}
		if (!CollectionUtils.isEmpty(page.getData())) {
			educationExperienceService
					.list(QueryBuilder.create(EducationExperienceMapping.class)
							.and(EducationExperienceMapping.MEMBERID, QueryOperator.IN, page.getData().stream().map(item->item.getId()).collect(Collectors.toList())).end()
							.sort(EducationExperienceMapping.GRADE).desc())
					.stream().collect(Collectors.groupingBy(EducationExperienceVO::getMemberId))
					.forEach((memberId, valueList) -> {
						page.getData().stream().filter(item -> item.getId().equals(memberId)).findAny().ifPresent(item -> {
							item.setEducationList(valueList);
						});
					});
		}
		return page;
	}
}
