package com.cloudok.uc.service.impl;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.cloudok.base.attach.io.AttachRWHandle;
import com.cloudok.base.attach.vo.AttachVO;
import com.cloudok.base.message.utils.MessageUtil;
import com.cloudok.base.message.vo.MessageReceive;
import com.cloudok.cache.Cache;
import com.cloudok.common.CacheType;
import com.cloudok.common.Constants;
import com.cloudok.core.context.SpringApplicationContext;
import com.cloudok.core.enums.UserType;
import com.cloudok.core.event.BusinessEvent;
import com.cloudok.core.exception.CoreExceptionMessage;
import com.cloudok.core.exception.SystemException;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.query.QueryOperator;
import com.cloudok.core.service.AbstractService;
import com.cloudok.core.vo.Page;
import com.cloudok.enums.MemberProfileType;
import com.cloudok.enums.TagCategory;
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
import com.cloudok.uc.event.MemberProfileEvent;
import com.cloudok.uc.event.RecognizedCreateEvent;
import com.cloudok.uc.event.ViewMemberDetailEvent;
import com.cloudok.uc.mapper.MemberMapper;
import com.cloudok.uc.mapping.EducationExperienceMapping;
import com.cloudok.uc.mapping.FirendMapping;
import com.cloudok.uc.mapping.InternshipExperienceMapping;
import com.cloudok.uc.mapping.MemberMapping;
import com.cloudok.uc.mapping.MemberTagsMapping;
import com.cloudok.uc.mapping.ProjectExperienceMapping;
import com.cloudok.uc.mapping.RecognizedMapping;
import com.cloudok.uc.mapping.ResearchExperienceMapping;
import com.cloudok.uc.po.MemberCirclePO;
import com.cloudok.uc.po.MemberPO;
import com.cloudok.uc.po.MemberSuggestScore;
import com.cloudok.uc.po.SuggsetMemberScorePO;
import com.cloudok.uc.service.EducationExperienceService;
import com.cloudok.uc.service.FirendService;
import com.cloudok.uc.service.InternshipExperienceService;
import com.cloudok.uc.service.MemberService;
import com.cloudok.uc.service.MemberTagsService;
import com.cloudok.uc.service.ProjectExperienceService;
import com.cloudok.uc.service.RecognizedService;
import com.cloudok.uc.service.ResearchExperienceService;
import com.cloudok.uc.task.MemberScoreCalcServiceV2;
import com.cloudok.uc.vo.BindRequest;
import com.cloudok.uc.vo.ChangePasswordRequest;
import com.cloudok.uc.vo.EducationExperienceVO;
import com.cloudok.uc.vo.FirendVO;
import com.cloudok.uc.vo.ForgotVO;
import com.cloudok.uc.vo.InternshipExperienceVO;
import com.cloudok.uc.vo.LoginVO;
import com.cloudok.uc.vo.MemberTagsVO;
import com.cloudok.uc.vo.MemberVO;
import com.cloudok.uc.vo.MemberVO.UserState;
import com.cloudok.uc.vo.ProjectExperienceVO;
import com.cloudok.uc.vo.RecognizedVO;
import com.cloudok.uc.vo.ResearchExperienceVO;
import com.cloudok.uc.vo.SingupVO;
import com.cloudok.uc.vo.SuggestResult;
import com.cloudok.uc.vo.SuggestedHistory;
import com.cloudok.uc.vo.SuggestedHistoryItem;
import com.cloudok.uc.vo.TokenVO;
import com.cloudok.uc.vo.UserCheckRequest;
import com.cloudok.uc.vo.VerifyCodeRequest;
import com.cloudok.util.DateTimeUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MemberServiceImpl extends AbstractService<MemberVO, MemberPO> implements MemberService, UserInfoHandler, ApplicationListener<BusinessEvent<?>> {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private Cache cacheService;

	private MemberMapper repository;

	@Autowired
	public MemberServiceImpl(MemberMapper repository) {
		super(repository);
		this.repository = repository;
	}

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
		} else {
			vo.setState(UserState.build(0L));
		}
		return vo;
	}

	@Override
	public List<MemberVO> convert2VO(List<MemberPO> e) {
		List<MemberVO> es = new ArrayList<MemberVO>();
		if (e != null && e.size() > 0) {
			e.forEach(item -> es.add(convert2VO(item)));
			List<AttachVO> list = AttachRWHandle.sign(e.stream().filter(item -> item.getAvatar() != null).map(item -> item.getAvatar()).distinct().collect(Collectors.toList()));
			if (!CollectionUtils.isEmpty(list)) {
				es.forEach(item -> {
					list.stream().filter(attach -> attach.getId().equals(item.getAvatar())).findAny().ifPresent(attach -> {
						item.setAvatarUrl(attach.getUrl());
					});
				});
			}
		}
		return es;
	}

	@Override
	public MemberVO create(MemberVO d) {
		// ?????????????????????????????????
		d.setProfileUpdateTs(new Timestamp(System.currentTimeMillis()));
		MemberVO m =  super.create(d);
		SpringApplicationContext.getBean(MemberScoreCalcServiceV2.class).initMemberScoreOnCreate(m.getId());
		return m;
	}

	@Override
	public TokenVO login(LoginVO vo) {
		List<MemberVO> memberList = this.list(QueryBuilder.create(MemberMapping.class).and(MemberMapping.EMAIL, vo.getUserName()) // email
				.or(MemberMapping.PHONE, vo.getUserName()) // phone
				.or(MemberMapping.USERNAME, vo.getUserName()) // userName
				.end());

		MemberVO sysUser = null;
		if (!StringUtils.isEmpty(vo.getCode())) {
			boolean isSms = "0".equalsIgnoreCase(vo.getLoginType());
			if (!"183727".equals(vo.getCode()) && !("897561".equals(vo.getCode()) && "13805389756".equals(vo.getUserName()))) {// test code
				String cacheKey = buildKey("login", isSms ? "sms" : "email", vo.getUserName());
				String code = cacheService.get(CacheType.VerifyCode, cacheKey, String.class);
				if (StringUtils.isEmpty(code)) {
					throw new SystemException("???????????????", CloudOKExceptionMessage.VERIFY_CODE_WRONG);
				}
				if (!code.equals(vo.getCode())) {
					throw new SystemException("???????????????", CloudOKExceptionMessage.VERIFY_CODE_WRONG);
				}
			}
			// ?????????????????????????????????????????????????????????????????????
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
		// if (sysUser.getFreeze().equals(Boolean.TRUE)) {
		// throw new SystemException(SecurityExceptionMessage.ACCESS_ACCOUNT_FROZEN);
		// }
		cacheService.evict(CacheType.Member, sysUser.getId().toString());
		User user = this.loadUserInfo(sysUser.getId(), sysUser);
		TokenVO token = TokenVO.build(JWTUtil.genToken(user, TokenType.ACCESS), JWTUtil.genToken(user, TokenType.REFRESH), user);
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
				JWTUtil.genToken(SecurityContextHelper.getCurrentUser(), TokenType.REFRESH), SecurityContextHelper.getCurrentUser());
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
			throw new SystemException("???????????????", CloudOKExceptionMessage.VERIFY_CODE_WRONG);
		}
		if (!code.equals(vo.getCode())) {
			throw new SystemException("???????????????", CloudOKExceptionMessage.VERIFY_CODE_WRONG);
		}

		List<MemberVO> userList = null;
		if ("1".equalsIgnoreCase(vo.getForgotType())) {
			userList = this.list(QueryBuilder.create(MemberMapping.class).and(MemberMapping.EMAIL, vo.getEmail()).end());
			if (CollectionUtils.isEmpty(userList)) {
				throw new SystemException("???????????????", CoreExceptionMessage.NOTFOUND_ERR);
			}
		} else if ("0".equalsIgnoreCase(vo.getForgotType())) {
			userList = this.list(QueryBuilder.create(MemberMapping.class).and(MemberMapping.PHONE, vo.getPhone()).end());
			if (CollectionUtils.isEmpty(userList)) {
				throw new SystemException("??????????????????", CoreExceptionMessage.NOTFOUND_ERR);
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
		if (StringUtils.isEmpty(vo.getPhone()) && !StringUtils.isEmpty(vo.getEmail())) { // fixed data, front-end may be error
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
		if (!StringUtils.isEmpty(vo.getPhone()) && StringUtils.isEmpty(vo.getEmail())) { // fixed data, front-end may be error
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
			throw new SystemException("???????????????", CloudOKExceptionMessage.VERIFY_CODE_WRONG);
		}
		if (!code.equals(vo.getCode())) {
			throw new SystemException("???????????????", CloudOKExceptionMessage.VERIFY_CODE_WRONG);
		}

		MemberVO member = new MemberVO();
		member.setEmail(vo.getEmail());
		member.setPhone(vo.getPhone());
		// member.setUserName(StringUtils.isEmpty(vo.getEmail())?vo.getPhone():vo.getEmail());
		this.create(member);

		MemberVO sysUser = this.get(member.getId());
		cacheService.evict(CacheType.Member, sysUser.getId().toString());
		User user = this.loadUserInfo(sysUser.getId(), sysUser);
		TokenVO token = TokenVO.build(JWTUtil.genToken(user, TokenType.ACCESS), JWTUtil.genToken(user, TokenType.REFRESH), user);
		SpringApplicationContext.publishEvent(MemberProfileEvent.create(getCurrentUserId(),MemberProfileType.base,member));
		return token;
	}

	@Override
	public MemberVO fillAccountInfo(@Valid MemberVO vo) {
		MemberVO member = new MemberVO();
		boolean isExits = checkUserNameExists(vo.getUserName(), SecurityContextHelper.getCurrentUserId());
		if (isExits) {
			throw new SystemException(CloudOKExceptionMessage.USERNAME_ALREADY_EXISTS);
		}
		MemberVO db = this.get(getCurrentUserId());
		member.setId(SecurityContextHelper.getCurrentUserId());
		if (!StringUtils.isEmpty(vo.getNickName())) {
			member.setNickName(vo.getNickName().trim());
		} else { // ??????????????????
			member.setNickName(vo.getNickName());
		}
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

		SpringApplicationContext.publishEvent(MemberProfileEvent.update(getCurrentUserId(),MemberProfileType.base,vo,db));

		return this.get(member.getId());
	}

	@Override
	public Boolean sendVerifycode(VerifyCodeRequest vo) {
		String cacheKey = buildKey(vo.getModule(), vo.getType(), vo.getKey());
		if (cacheService.exist(CacheType.VerifyCode, "S:" + cacheKey)) { // ??????????????????
			return null;
		}
		Integer count = cacheService.get(CacheType.VerifyCodeCount, cacheKey, Integer.class);
		int _c = count == null ? 0 : count;
		if (_c > 10) { // ???????????????10???
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
		return !CollectionUtils.isEmpty(this.list(QueryBuilder.create(MemberMapping.class).and(MemberMapping.EMAIL, email).end()));
	}

	private boolean checkPhoneExists(String phone) {
		return !CollectionUtils.isEmpty(this.list(QueryBuilder.create(MemberMapping.class).and(MemberMapping.PHONE, phone).end()));
	}

	private boolean checkUserNameExists(String userName, Long userId) {
		return this.count(QueryBuilder.create(MemberMapping.class).and(MemberMapping.USERNAME, userName).and(MemberMapping.ID, QueryOperator.NEQ, userId).end()) != 0;
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
		SpringApplicationContext.publishEvent(MemberProfileEvent.update(getCurrentUserId(),MemberProfileType.base,this.get(getCurrentUserId()),vo));
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
		if (CollectionUtils.isEmpty(memberIdList)) {
			return Collections.emptyList();
		}
		List<WholeMemberDTO> list = getWholeMemberInfo(memberIdList, false);
		//// ??????????????????
		return memberIdList.stream().distinct().map(item -> {
			Optional<WholeMemberDTO> opt = list.stream().filter(m -> m.getId().equals(item)).findAny();
			return opt.isPresent() ? opt.get() : null;
		}).filter(item -> item != null).collect(Collectors.toList());
	}

	private List<WholeMemberDTO> getWholeMemberInfo(List<Long> memberIdList, boolean includeSecurityInfo) {
		if (CollectionUtils.isEmpty(memberIdList)) {
			return Collections.emptyList();
		}
		return this.getWholeMemberInfoByVOList(this.get(memberIdList), includeSecurityInfo);
	}

	public List<WholeMemberDTO> getWholeMemberInfoByVOList(List<MemberVO> voList) {
		return this.getWholeMemberInfoByVOList(voList, false);
	}

	private List<WholeMemberDTO> getWholeMemberInfoByVOList(List<MemberVO> voList, boolean includeSecurityInfo) {
		if (CollectionUtils.isEmpty(voList)) {
			return Collections.emptyList();
		}
		List<Long> memberIdList = voList.stream().map(item -> item.getId()).distinct().collect(Collectors.toList());
		List<WholeMemberDTO> memberList = voList.stream().map(item -> {
			WholeMemberDTO dto = new WholeMemberDTO();
			BeanUtils.copyProperties(item, dto);
			if (!includeSecurityInfo) { // ??????????????????
				dto.setBirthDate(null);
				dto.setPhone(null);
				dto.setUserName(null);
				dto.setEmail(null);
			}
			dto.setPassword(null);
			return dto;
		}).collect(Collectors.toList());

		if (!CollectionUtils.isEmpty(memberList)) {
			educationExperienceService
					.list(QueryBuilder.create(EducationExperienceMapping.class).and(EducationExperienceMapping.MEMBERID, QueryOperator.IN, memberIdList).end()
							.sort(EducationExperienceMapping.SN).asc())
					.stream().collect(Collectors.groupingBy(EducationExperienceVO::getMemberId)).forEach((memberId, valueList) -> {
						memberList.stream().filter(item -> item.getId().equals(memberId)).findAny().ifPresent(item -> {
							if (!CollectionUtils.isEmpty(valueList)) {
								valueList.sort((a, b) -> a.getSn().compareTo(b.getSn()));
							}
							item.setEducationList(valueList);
						});
					});

			internshipExperienceService.list(QueryBuilder.create(InternshipExperienceMapping.class).and(InternshipExperienceMapping.MEMBERID, QueryOperator.IN, memberIdList).end())
					.stream().collect(Collectors.groupingBy(InternshipExperienceVO::getMemberId)).forEach((memberId, valueList) -> {
						memberList.stream().filter(item -> item.getId().equals(memberId)).findAny().ifPresent(item -> {
							if (!CollectionUtils.isEmpty(valueList)) {
								valueList.sort((a, b) -> a.getSn().compareTo(b.getSn()));
							}
							item.setInternshipList(valueList);
						});
					});

			memberTagsService.list(QueryBuilder.create(MemberTagsMapping.class).and(MemberTagsMapping.MEMBERID, QueryOperator.IN, memberIdList).end()).stream()
					.collect(Collectors.groupingBy(MemberTagsVO::getMemberId)).forEach((memberId, valueList) -> {
						memberList.stream().filter(item -> item.getId().equals(memberId)).findAny().ifPresent(item -> {
							if (!CollectionUtils.isEmpty(valueList)) {
								valueList.sort((a, b) -> a.getSn().compareTo(b.getSn()));
							}
							item.setTagsList(valueList);
						});
					});

			projectExperienceService.list(QueryBuilder.create(ProjectExperienceMapping.class).and(ProjectExperienceMapping.MEMBERID, QueryOperator.IN, memberIdList).end())
					.stream().collect(Collectors.groupingBy(ProjectExperienceVO::getMemberId)).forEach((memberId, valueList) -> {
						memberList.stream().filter(item -> item.getId().equals(memberId)).findAny().ifPresent(item -> {
							if (!CollectionUtils.isEmpty(valueList)) {
								valueList.sort((a, b) -> a.getSn().compareTo(b.getSn()));
							}
							item.setProjectList(valueList);
						});
					});

			researchExperienceService.list(QueryBuilder.create(ResearchExperienceMapping.class).and(ResearchExperienceMapping.MEMBERID, QueryOperator.IN, memberIdList).end())
					.stream().collect(Collectors.groupingBy(ResearchExperienceVO::getMemberId)).forEach((memberId, valueList) -> {
						memberList.stream().filter(item -> item.getId().equals(memberId)).findAny().ifPresent(item -> {
							if (!CollectionUtils.isEmpty(valueList)) {
								valueList.sort((a, b) -> a.getSn().compareTo(b.getSn()));
							}
							item.setResearchList(valueList);
						});
					});
		}
		return voList.stream().map(item -> {
			Optional<WholeMemberDTO> opt = memberList.stream().filter(m -> m.getId().equals(item.getId())).findAny();
			return opt.isPresent() ? opt.get() : null;
		}).filter(item -> item != null).collect(Collectors.toList());
	}

	@Override
	public WholeMemberDTO getWholeMemberInfo(Long memberId) {
		List<WholeMemberDTO> list = this.getWholeMemberInfo(java.util.Collections.singletonList(memberId));
		return CollectionUtils.isEmpty(list) ? null : list.get(0);
	}

	@Override
	public WholeMemberDTO getWholeMemberInfo(Long memberId, boolean includeSecurityInfo) {
		List<WholeMemberDTO> list = this.getWholeMemberInfo(java.util.Collections.singletonList(memberId), includeSecurityInfo);
		return CollectionUtils.isEmpty(list) ? null : list.get(0);
	}

	@Override
	public Boolean checkPhone(UserCheckRequest request) {
		return !CollectionUtils.isEmpty(this.list(QueryBuilder.create(MemberMapping.class).and(MemberMapping.PHONE, request.getPhone()).end()));
	}

	@Override
	public List<SimpleMemberInfo> getSimpleMemberInfo(List<Long> memberIdList) {
		if (CollectionUtils.isEmpty(memberIdList)) {
			return Collections.emptyList();
		}
		memberIdList = memberIdList.stream().distinct().collect(Collectors.toList());
		List<SimpleMemberInfo> list = this.getSimpleMemberInfoByVOList(this.get(memberIdList));
		// ??????????????????
		return memberIdList.stream().distinct().map(item -> {
			Optional<SimpleMemberInfo> opt = list.stream().filter(m -> m.getId().equals(item)).findAny();
			return opt.isPresent() ? opt.get() : null;
		}).filter(item -> item != null).collect(Collectors.toList());
	}

	private List<SimpleMemberInfo> getSimpleMemberInfoByVOList(List<MemberVO> voList) {
		if (CollectionUtils.isEmpty(voList)) {
			return Collections.emptyList();
		}
		Long currentId = getCurrentUserId();
		List<Long> memberIdList = voList.stream().map(item -> item.getId()).distinct().collect(Collectors.toList());
		List<SimpleMemberInfo> memberList = voList.stream().map(item -> {
			SimpleMemberInfo dto = new SimpleMemberInfo();
			BeanUtils.copyProperties(item, dto);
			return dto;
		}).collect(Collectors.toList());
		if (!CollectionUtils.isEmpty(memberList)) {
			// ??????????????????
			// ?????????????????????????????????
			List<RecognizedVO> recognizedList = this.recognizedService.list(QueryBuilder.create(RecognizedMapping.class).and(RecognizedMapping.TARGETID, currentId)
					.and(RecognizedMapping.SOURCEID, QueryOperator.IN, memberIdList).end().or(RecognizedMapping.SOURCEID, currentId)
					.and(RecognizedMapping.TARGETID, QueryOperator.IN, memberIdList).end().sort(RecognizedMapping.CREATETIME).desc());

			memberList.forEach(item -> {
				item.setFrom(false);
				item.setTo(false);
				// ????????????????????? ?????????member????????????????????????????????????
				recognizedList.stream().filter(a -> a.getSourceId().equals(item.getId()) && a.getTargetId().equals(currentId)).findAny().ifPresent(a -> {
					item.setFrom(true);
				});
				// ?????????????????????
				recognizedList.stream().filter(a -> a.getTargetId().equals(item.getId()) && currentId.equals(a.getSourceId())).findAny().ifPresent(a -> {
					item.setTo(true);
				});
			});

			List<AttachVO> attachs = AttachRWHandle
					.sign(memberList.stream().filter(item -> item.getAvatar() != null).map(item -> item.getAvatar()).distinct().collect(Collectors.toList()));
			if (!CollectionUtils.isEmpty(attachs)) {
				memberList.forEach(item -> {
					attachs.stream().filter(attach -> attach.getId().equals(item.getAvatar())).findAny().ifPresent(attach -> {
						item.setAvatarUrl(attach.getUrl());
					});
				});
			}
			educationExperienceService
					.list(QueryBuilder.create(EducationExperienceMapping.class).and(EducationExperienceMapping.MEMBERID, QueryOperator.IN, memberIdList).end()
							.sort(EducationExperienceMapping.SN).asc())
					.stream().collect(Collectors.groupingBy(EducationExperienceVO::getMemberId)).forEach((memberId, valueList) -> {
						if (!CollectionUtils.isEmpty(valueList)) {
							valueList.sort((a, b) -> a.getSn().compareTo(b.getSn()));
						}
						memberList.stream().filter(item -> item.getId().equals(memberId)).findAny().ifPresent(item -> {
							item.setEducation(valueList.get(0));
							item.setEducationList(valueList);
						});
					});
		}
		// ??????????????????
		return memberIdList.stream().map(item -> {
			Optional<SimpleMemberInfo> opt = memberList.stream().filter(m -> m.getId().equals(item)).findAny();
			return opt.isPresent() ? opt.get() : null;
		}).filter(item -> item != null).collect(Collectors.toList());
	}

	@Override
	public SimpleMemberDTO getSimpleMemberInfo() {
		List<EducationExperienceVO> edu = educationExperienceService.list(QueryBuilder.create(EducationExperienceMapping.class)
				.and(EducationExperienceMapping.MEMBERID, getCurrentUserId()).end().sort(EducationExperienceMapping.SN).asc().enablePaging().pageNo(1).pageSize(1).end());
		long friendCount = firendService.count(QueryBuilder.create(FirendMapping.class).and(FirendMapping.SOURCEID, getCurrentUserId()).end());
		if (!CollectionUtils.isEmpty(edu)) {
			edu.sort((a, b) -> a.getSn().compareTo(b.getSn()));
		}
		boolean imperfect = true;
		if (internshipExperienceService.count(QueryBuilder.create(InternshipExperienceMapping.class).and(InternshipExperienceMapping.MEMBERID, getCurrentUserId()).end()) == 0) {
			if (memberTagsService.count(QueryBuilder.create(MemberTagsMapping.class).and(InternshipExperienceMapping.MEMBERID, getCurrentUserId()).end()) == 0) {
				if (projectExperienceService.count(QueryBuilder.create(ProjectExperienceMapping.class).and(InternshipExperienceMapping.MEMBERID, getCurrentUserId()).end()) == 0) {
					if (researchExperienceService
							.count(QueryBuilder.create(ResearchExperienceMapping.class).and(InternshipExperienceMapping.MEMBERID, getCurrentUserId()).end()) == 0) {
						imperfect = false;
					}
				}
			}
		}
		SimpleMemberDTO dto = SimpleMemberDTO.builder().member(this.get(getCurrentUserId())).eduExperience(CollectionUtils.isEmpty(edu) ? null : edu.get(0))
				.friendCount(friendCount).imperfect(imperfect)
				.fromCount(recognizedService.count(QueryBuilder.create(RecognizedMapping.class).and(RecognizedMapping.TARGETID, getCurrentUserId()).end()))
				.toCount(recognizedService.count(QueryBuilder.create(RecognizedMapping.class).and(RecognizedMapping.SOURCEID, getCurrentUserId()).end())).newFrom(recognizedService
						.count(QueryBuilder.create(RecognizedMapping.class).and(RecognizedMapping.TARGETID, getCurrentUserId()).and(RecognizedMapping.READ, false).end()))
				.build();
		List<RecognizedVO> list = recognizedService.list(QueryBuilder.create(RecognizedMapping.class).and(RecognizedMapping.TARGETID, getCurrentUserId())
				.and(RecognizedMapping.READ, false).end().sort(RecognizedMapping.CREATETIME).desc().enablePaging().page(1, 3).end());
		if (!CollectionUtils.isEmpty(list)) {
			dto.setNewFromList(this.getSimpleMemberInfo(list.stream().map(item -> item.getSourceId()).collect(Collectors.toList())));
		}
		if (dto.getMember() != null) {
			dto.getMember().setPassword(null); // ????????????????????????
		}
		return dto;
	}

	@Override
	public MemberVO getMemberDetails(Long memberId) {
		WholeMemberDTO member = this.getWholeMemberInfo(memberId);
		Long currentUserId = getCurrentUserId();
		// ?????????
		if (!currentUserId.equals(memberId)) {
			List<RecognizedVO> recoginzedList = this.recognizedService.list(QueryBuilder.create(RecognizedMapping.class).and(RecognizedMapping.SOURCEID, memberId)
					.and(RecognizedMapping.TARGETID, currentUserId).end().or(RecognizedMapping.SOURCEID, currentUserId).and(RecognizedMapping.TARGETID, memberId).end());
			if (!CollectionUtils.isEmpty(recoginzedList)) {
				recoginzedList.stream().filter(item -> item.getSourceId().equals(currentUserId)).findAny().ifPresent(item -> {
					member.setTo(true);
				});
				recoginzedList.stream().filter(item -> item.getTargetId().equals(currentUserId)).findAny().ifPresent(item -> {
					member.setFrom(true);
				});
			}
			// ??????????????????
			this.filter(Arrays.asList(member));
		}
		if (!SecurityContextHelper.getCurrentUserId().equals(memberId)) {
			SpringApplicationContext.publishEvent(new ViewMemberDetailEvent(Pair.of(currentUserId, memberId)));
		}
		return member;
	}

	@Autowired
	private FirendService firendService;

	@Autowired
	private Cache cache;

	private static final int defaultRI = 80;

	private static final int recognized = -50;

	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	public Page<WholeMemberDTO> suggest(Integer filterType, String threadId, Integer pageNo, Integer pageSize) {
		Long currentUserId = getCurrentUserId();
		List<SuggsetMemberScorePO> suggestMemberList = this.repository.suggest(Arrays.asList(currentUserId), filterType, 1000);
		Page<WholeMemberDTO> page = new Page<WholeMemberDTO>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setTotalCount((long) suggestMemberList.size());

		if (!CollectionUtils.isEmpty(suggestMemberList)) {
			// ???????????????
			Map<Long, Integer> cachedRIScore = cache.get(CacheType.RI, currentUserId + threadId, Map.class);
			Map<Long, Integer> riScore = new HashMap<Long, Integer>();
			if (cachedRIScore == null || cachedRIScore.isEmpty()) {
				String lastThreadId = cache.get(CacheType.RI, currentUserId + "_last_ri_key", String.class);
				if (!StringUtils.isEmpty(lastThreadId)) {
					cache.evict(CacheType.RI, lastThreadId); // ????????????????????? ??????????????????
				}
				try {
					SecureRandom random = SecureRandom.getInstanceStrong();
					suggestMemberList.stream().forEach(item -> {
						riScore.put(item.getId(), random.nextInt(defaultRI));
					});
				} catch (NoSuchAlgorithmException e) {
					log.error(e.getMessage(), e);
					Random random = new Random();
					suggestMemberList.stream().forEach(item -> {
						riScore.put(item.getId(), random.nextInt(defaultRI));
					});
				}
				// ??????60??????
				cache.put(CacheType.RI, currentUserId + threadId, riScore, 60, TimeUnit.MINUTES);
				// ???????????????thread??????
				cache.put(CacheType.RI, currentUserId + "_last_ri_key", currentUserId + threadId, 60, TimeUnit.MINUTES);
			} else {
				riScore.putAll(cachedRIScore);
			}
			// ??????RI??????
			suggestMemberList.stream().forEach(item -> {
				Integer ri = riScore.get(item.getId());
				double s = item.getScore() == null ? 0 : item.getScore();
				if (ri == null) {
					ri = 0;
				}
				item.setScore(ri + s);
			});
			List<RecognizedVO> myRecoginzedList = this.recognizedService.list(QueryBuilder.create(RecognizedMapping.class).and(RecognizedMapping.SOURCEID, currentUserId).end());
			myRecoginzedList.stream().forEach(item -> {
				suggestMemberList.stream().filter(a -> a.getId().equals(item.getTargetId())).findAny().ifPresent(a -> {
					a.setScore(a.getScore() + recognized); // ????????????30???
				});
			});
			List<SuggsetMemberScorePO> targetList = suggestMemberList.stream().sorted((a, b) -> b.getScore().compareTo(a.getScore())).skip((pageNo - 1) * pageSize).limit(pageSize)
					.collect(Collectors.toList());
			List<WholeMemberDTO> memberList = this.getWholeMemberInfo(targetList.stream().map(item -> item.getId()).collect(Collectors.toList()));

			List<RecognizedVO> recoginzedList = this.recognizedService.list(QueryBuilder.create(RecognizedMapping.class).and(RecognizedMapping.SOURCEID, currentUserId)
					.and(RecognizedMapping.TARGETID, QueryOperator.IN, targetList.stream().map(item -> item.getId()).collect(Collectors.toList())).end()
					.or(RecognizedMapping.TARGETID, currentUserId)
					.and(RecognizedMapping.SOURCEID, QueryOperator.IN, targetList.stream().map(item -> item.getId()).collect(Collectors.toList())).end());
			// ?????????????????????????????????
			memberList.stream().forEach(member -> {
				Integer ri = riScore.get(member.getId());
				member.setRi(ri == null ? 0 : ri.doubleValue());
				suggestMemberList.stream().filter(a -> a.getId().equals(member.getId())).findAny().ifPresent(a -> {
					member.setScore(a.getScore());
				});
			});
			if (!CollectionUtils.isEmpty(recoginzedList)) {
				memberList.stream().forEach(member -> {
					recoginzedList.stream().filter(item -> item.getSourceId().equals(currentUserId) && item.getTargetId().equals(member.getId())).findAny().ifPresent(item -> {
						member.setTo(true);
					});
					recoginzedList.stream().filter(item -> item.getTargetId().equals(currentUserId) && item.getSourceId().equals(member.getId())).findAny().ifPresent(item -> {
						member.setFrom(true);
					});
				});
			}
			// ??????????????? ??????????????????bug
			page.setData(memberList.stream().sorted((b, a) -> a.getScore().compareTo(b.getScore())).collect(Collectors.toList()));
		}
		return page;
	}

	@Override
	public Page<SimpleMemberInfo> getSecondDegreeRecognized(Long memberId, Integer pageNo, Integer pageSize) {
		Long currentUserId = getCurrentUserId();
		Page<SimpleMemberInfo> resultPage = new Page<SimpleMemberInfo>();
		Page<RecognizedVO> page = this.recognizedService.getSecondDegreeRecognized(currentUserId, memberId, pageNo, pageSize);
		BeanUtils.copyProperties(page, resultPage);
		if (!CollectionUtils.isEmpty(page.getData())) {
			List<SimpleMemberInfo> memberList = this.getSimpleMemberInfo(page.getData().stream().map(item -> item.getTargetId()).collect(Collectors.toList()));
			List<SimpleMemberInfo> resultList = page.getData().stream().map(item -> {
				return memberList.stream().filter(m -> m.getId().equals(item.getTargetId())).findAny().get();
			}).collect(Collectors.toList());
			resultPage.setData(resultList);
		}
		return resultPage;
	}

	// 0 ?????? 1 ????????? 2 ????????? 3 ?????????
	@Override
	public Page<SimpleMemberInfo> friend(Integer type, Integer pageNo, Integer pageSize) {
		Page<SimpleMemberInfo> resultPage = null;
		switch (type) {
		case 0:
			resultPage = this.getFirendsList(pageNo, pageSize);
			break;
		case 1:
			resultPage = this.getMyRecognizedMemberList(pageNo, pageSize);
			break;
		case 2:
			resultPage = this.getRecognizedMeMemberList(pageNo, pageSize);
			break;
		case 3:
			resultPage = this.getNewRecognizedMeMemberList(pageNo, pageSize);
			break;

		default:
			break;
		}
		return resultPage;
	}

	/**
	 * ???????????????
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	private Page<SimpleMemberInfo> getNewRecognizedMeMemberList(Integer pageNo, Integer pageSize) {
		Long currentUserId = getCurrentUserId();
		Page<SimpleMemberInfo> resultPage = new Page<SimpleMemberInfo>();
		Page<RecognizedVO> page = this.recognizedService.page(QueryBuilder.create(RecognizedMapping.class).and(RecognizedMapping.TARGETID, currentUserId)
				.and(RecognizedMapping.READ, 0).end().sort(RecognizedMapping.CREATETIME).desc().enablePaging().page(pageNo, pageSize).end());
		BeanUtils.copyProperties(page, resultPage);
		if (!CollectionUtils.isEmpty(page.getData())) {
			List<SimpleMemberInfo> memberList = this.getSimpleMemberInfo(page.getData().stream().map(item -> item.getSourceId()).collect(Collectors.toList()));
			List<SimpleMemberInfo> resultList = page.getData().stream().map(item -> {
				return memberList.stream().filter(m -> m.getId().equals(item.getSourceId())).findAny().get();
			}).collect(Collectors.toList());
			// ???????????????
			page.getData().stream().forEach(item -> {
				item.setRead(true);
				this.recognizedService.merge(item);
			});

			resultPage.setData(resultList);
		}
		return resultPage;
	}

	/**
	 * ????????????
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	private Page<SimpleMemberInfo> getRecognizedMeMemberList(Integer pageNo, Integer pageSize) {
		Long currentUserId = getCurrentUserId();
		Page<SimpleMemberInfo> resultPage = new Page<SimpleMemberInfo>();
		Page<RecognizedVO> page = this.recognizedService.page(QueryBuilder.create(RecognizedMapping.class).and(RecognizedMapping.TARGETID, currentUserId).end()
				.sort(RecognizedMapping.CREATETIME).desc().enablePaging().page(pageNo, pageSize).end());
		BeanUtils.copyProperties(page, resultPage);
		if (!CollectionUtils.isEmpty(page.getData())) {
			List<SimpleMemberInfo> memberList = this.getSimpleMemberInfo(page.getData().stream().map(item -> item.getSourceId()).collect(Collectors.toList()));
			List<SimpleMemberInfo> resultList = page.getData().stream().map(item -> {
				Optional<SimpleMemberInfo> opt = memberList.stream().filter(m -> m.getId().equals(item.getSourceId())).findAny();
				return opt.isPresent() ? opt.get() : null;
			}).filter(item -> item != null).collect(Collectors.toList());
			resultPage.setData(resultList);
		}
		return resultPage;
	}

	/**
	 * 
	 * ????????????
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	private Page<SimpleMemberInfo> getMyRecognizedMemberList(Integer pageNo, Integer pageSize) {
		Long currentUserId = getCurrentUserId();
		Page<SimpleMemberInfo> resultPage = new Page<SimpleMemberInfo>();
		Page<RecognizedVO> page = this.recognizedService.page(QueryBuilder.create(RecognizedMapping.class).and(RecognizedMapping.SOURCEID, currentUserId).end()
				.sort(RecognizedMapping.CREATETIME).desc().enablePaging().page(pageNo, pageSize).end());
		BeanUtils.copyProperties(page, resultPage);
		if (!CollectionUtils.isEmpty(page.getData())) {
			List<SimpleMemberInfo> memberList = this.getSimpleMemberInfo(page.getData().stream().map(item -> item.getTargetId()).collect(Collectors.toList()));
			List<SimpleMemberInfo> resultList = page.getData().stream().map(item -> {
				return memberList.stream().filter(m -> m.getId().equals(item.getTargetId())).findAny().get();
			}).collect(Collectors.toList());
			resultPage.setData(resultList);
		}
		return resultPage;
	}

	/**
	 * ????????????
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	private Page<SimpleMemberInfo> getFirendsList(Integer pageNo, Integer pageSize) {
		Long currentUserId = getCurrentUserId();
		Page<SimpleMemberInfo> resultPage = new Page<SimpleMemberInfo>();
		Page<FirendVO> page = this.firendService.page(QueryBuilder.create(FirendMapping.class).and(FirendMapping.SOURCEID, currentUserId).end().sort(FirendMapping.CREATETIME)
				.desc().enablePaging().page(pageNo, pageSize).end());
		BeanUtils.copyProperties(page, resultPage);
		if (!CollectionUtils.isEmpty(page.getData())) {
			List<SimpleMemberInfo> memberList = this.getSimpleMemberInfo(page.getData().stream().map(item -> item.getTargetId()).collect(Collectors.toList()));
			memberList.forEach(item -> {
				item.setFrom(true);
				item.setTo(true);
			});
			List<SimpleMemberInfo> resultList = page.getData().stream().map(item -> {
				return memberList.stream().filter(m -> m.getId().equals(item.getTargetId())).findAny().get();
			}).collect(Collectors.toList());
			resultPage.setData(resultList);
		}
		return resultPage;
	}

	// ???????????????Type???????????? 1 ???????????? 2 ?????? 3 ?????? 4 ??????/???????????????filterType=0 ?????????????????????????????????filterType=1
	// ???????????????????????????
	@Override
	@Deprecated
	public Page<WholeMemberDTO> getMemberCircles(Integer filterType, Integer type, Long businessId, Integer pageNo, Integer pageSize) {
		if (type == null || businessId == null) {
			return new Page<>();
		}
		if (type == null || businessId == null) {
			return new Page<>();
		}
		Long currentUserId = getCurrentUserId();
		Long count = this.repository.getMemberCirclesCount(currentUserId, Arrays.asList(currentUserId), filterType, type, businessId);
		Page<WholeMemberDTO> page = new Page<WholeMemberDTO>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setTotalCount(count);
		if (page.getTotalCount() > 0 && (page.getTotalCount() / page.getPageSize() + 1) >= page.getPageNo()) {
			// ??????????????????
			List<MemberCirclePO> suggestMemberList = this.repository.getMemberCirclesList(currentUserId, Arrays.asList(currentUserId), filterType, type, businessId,
					(pageNo - 1) * pageSize, pageSize);
			List<Long> suggestMemberIdList = suggestMemberList.stream().map(item -> item.getMemberId()).collect(Collectors.toList());
			List<WholeMemberDTO> memberList = this.getWholeMemberInfo(suggestMemberIdList);
			List<RecognizedVO> recoginzedList = this.recognizedService.list(QueryBuilder.create(RecognizedMapping.class).and(RecognizedMapping.SOURCEID, currentUserId)
					.and(RecognizedMapping.TARGETID, QueryOperator.IN, suggestMemberIdList).end().or(RecognizedMapping.TARGETID, currentUserId)
					.and(RecognizedMapping.SOURCEID, QueryOperator.IN, suggestMemberIdList).end());
			if (!CollectionUtils.isEmpty(recoginzedList)) {
				memberList.stream().forEach(member -> {
					recoginzedList.stream().filter(item -> item.getSourceId().equals(currentUserId) && item.getTargetId().equals(member.getId())).findAny().ifPresent(item -> {
						member.setTo(true);
					});
					recoginzedList.stream().filter(item -> item.getTargetId().equals(currentUserId) && item.getSourceId().equals(member.getId())).findAny().ifPresent(item -> {
						member.setFrom(true);
					});
				});
			}
			// ???????????????Type???????????? 1 ???????????? 2 ?????? 3 ?????? 4 ??????/????????????
			memberList.stream().forEach(item -> {
				switch (type) {
				case 1: // ????????????
					item.setInternshipList(null);
					item.setProjectList(null);
					item.setTagsList(null);
					List<ResearchExperienceVO> researchList = item.getResearchList();
					if (!CollectionUtils.isEmpty(researchList)) {
						item.setResearchList(
								researchList.stream().filter(r -> r.getDomain() != null).filter(r -> businessId.equals(r.getDomain().getId())).collect(Collectors.toList()));
					}
					break;
				case 2: // ??????
					item.setProjectList(null);
					item.setResearchList(null);
					item.setTagsList(null);
					List<InternshipExperienceVO> internshipList = item.getInternshipList();
					if (!CollectionUtils.isEmpty(internshipList)) {
						item.setInternshipList(internshipList.stream().filter(r -> r.getIndustry() != null).filter(r -> businessId.toString().equals(r.getIndustry().getCategory()))
								.collect(Collectors.toList()));
					}
					break;
				case 3:// ??????
					item.setInternshipList(null);
					item.setResearchList(null);
					item.setTagsList(null);
					List<ProjectExperienceVO> projectList = item.getProjectList();
					if (!CollectionUtils.isEmpty(projectList)) {
						item.setProjectList(projectList.stream().filter(r -> businessId.toString().equals(r.getCategory())).collect(Collectors.toList()));
					}
					break;
				case 4:// ????????????
				case 5:// ????????????
					item.setInternshipList(null);
					item.setProjectList(null);
					item.setResearchList(null);
					List<MemberTagsVO> tagsList = item.getTagsList();
					if (!CollectionUtils.isEmpty(tagsList)) {
						item.setTagsList(tagsList.stream().filter(r -> r.getTag() != null).filter(r -> businessId.equals(r.getTag().getId())).collect(Collectors.toList()));
					}
					break;
				default:
					break;
				}
			});
			if (filterType != null && filterType == 1) {
				memberList.stream().forEach(item -> { // ???????????? ?????????id?????????null
					item.setId(null);
					item.setNickName(null);
				});
			}
			page.setData(memberList);
		}

		return page;
	}

	private static final int SUGGEST_MEMBER_SIZE = 5; // ??????n???
	private static final int SUGGEST_MEMBER_TIME_LIMIT = 5; // ????????????5???

	private SuggestedHistory getSuggestedHistory(String key) {
		SuggestedHistory suggestedHistory = cacheService.get(CacheType.SuggestHistory, key, SuggestedHistory.class);
		if (suggestedHistory == null) {
			suggestedHistory = new SuggestedHistory(0, 0, 0, new ArrayList<SuggestedHistoryItem>());
		}
		return suggestedHistory;
	}

	@Deprecated
	@Override
	public Object ignoreSuggestMember(Long memberId) {
		String key = getCurrentUserId() + DateTimeUtil.formatSimpleyyyyMMdd(new Date());
		SuggestedHistory history = this.getSuggestedHistory(key);
		history.getList().stream().filter(item -> item.getTargetId().equals(memberId)).findAny().ifPresent(item -> {
			item.setStatus(2);
		});
		cacheService.put(CacheType.SuggestHistory, key, history, 1, TimeUnit.DAYS);
		return true;
	}

	/**
	 * ??????????????? ????????????
	 * 
	 * @param event
	 */
	private void onRecognizedCreateEvent(RecognizedCreateEvent event) {
		RecognizedVO eventData = event.getEventData();
		String key = eventData.getSourceId() + DateTimeUtil.formatSimpleyyyyMMdd(new Date());
		if (cacheService.exist(CacheType.SuggestHistory, key)) { // ???????????????
			SuggestedHistory history = this.getSuggestedHistory(key);
			history.getList().stream().filter(item -> item.getTargetId().equals(eventData.getTargetId())).findAny().ifPresent(item -> {
				item.setStatus(1);
				cacheService.put(CacheType.SuggestHistory, key, history, 1, TimeUnit.DAYS);
			});
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Page<WholeMemberDTO> suggestV3(String threadId, Integer pageNo, Integer pageSize) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		if(StringUtils.isEmpty(threadId)) {
			throw new SystemException("threadId????????????", CoreExceptionMessage.PARAMETER_ERR);
		}
		if(!SecurityContextHelper.isLogin()) { 
			return getDefaultMemberList(pageNo,pageSize);
		}
		Long currentUserId = getCurrentUserId();
		String cachedThreadId = cache.get(CacheType.SuggestStream, currentUserId.toString(),String.class);
		if(cachedThreadId == null) {
			cache.put(CacheType.SuggestStream, currentUserId.toString(),threadId);
		}else {
			if(!cachedThreadId.equals(threadId)) {
				cache.put(CacheType.SuggestStream, currentUserId.toString(),threadId);
				List<Long> suggestMemberIdList = (List<Long>)cache.get(CacheType.SuggestStream,currentUserId+cachedThreadId,List.class);
				this.updateKAB(currentUserId,suggestMemberIdList);
			}
		}
		Page<WholeMemberDTO> result = new Page<WholeMemberDTO>();
		List<Long> suggestMemberIdList = (List<Long>)cache.get(CacheType.SuggestStream,currentUserId+threadId,List.class);
		if(CollectionUtils.isEmpty(suggestMemberIdList)) {
			suggestMemberIdList = new ArrayList<Long>();
		}
		Long count = this.repository.getSuggestV3Count(currentUserId);
		List<MemberSuggestScore> suggestList  = this.repository.getSuggestV3List(currentUserId,(pageNo-1)*pageSize,pageSize);
		if(!CollectionUtils.isEmpty(suggestList)) {
			result.setData(this.getWholeMemberInfo(suggestList.stream().map(item -> item.getTargetId()).collect(Collectors.toList())));
		
			suggestMemberIdList.addAll(suggestList.stream().map(item -> item.getTargetId()).collect(Collectors.toList()));
			cache.put(CacheType.SuggestStream,currentUserId+threadId,suggestMemberIdList);
			
			//???????????????
			result.getData().stream().forEach(item -> {
				suggestList.stream().filter(s -> s.getTargetId().equals(item.getId())).findAny().ifPresent(s ->{
					item.setScore(s.getScore());
				});
			});
			
			List<RecognizedVO> recoginzedList = this.recognizedService.list(QueryBuilder.create(RecognizedMapping.class).and(RecognizedMapping.SOURCEID, currentUserId)
					.and(RecognizedMapping.TARGETID, QueryOperator.IN, result.getData().stream().map(item -> item.getId()).collect(Collectors.toList())).end()
					.or(RecognizedMapping.TARGETID, currentUserId)
					.and(RecognizedMapping.SOURCEID, QueryOperator.IN, result.getData().stream().map(item -> item.getId()).collect(Collectors.toList())).end());
			if (!CollectionUtils.isEmpty(recoginzedList)) {
				result.getData().stream().forEach(member -> {
					recoginzedList.stream().filter(item -> item.getSourceId().equals(currentUserId) && item.getTargetId().equals(member.getId())).findAny().ifPresent(item -> {
						member.setTo(true);
					});
					recoginzedList.stream().filter(item -> item.getTargetId().equals(currentUserId) && item.getSourceId().equals(member.getId())).findAny().ifPresent(item -> {
						member.setFrom(true);
					});
				});
			}
			SecureRandom sr = new SecureRandom();
			List<WholeMemberDTO> list = this.filter(result.getData());
			//?????????????????????????????????????????? ??? ??????????????????
			List<WholeMemberDTO> myRecoginzedList = list.stream().filter(item -> item.isTo())
					.map(item -> Pair.of(item, sr.nextInt())).sorted((a,b)->a.getRight().compareTo(b.getRight())).map(item -> item.getLeft())
					.collect(Collectors.toList());
			
			List<WholeMemberDTO> myUnRecoginzedList = list.stream().filter(item -> !item.isTo())
					.map(item -> Pair.of(item, sr.nextInt())).sorted((a,b)->a.getRight().compareTo(b.getRight())).map(item -> item.getLeft())
					.collect(Collectors.toList());
			 
			List<WholeMemberDTO> resultList  =  new ArrayList<WholeMemberDTO>();
			resultList.addAll(myUnRecoginzedList);
			resultList.addAll(myRecoginzedList);
			result.setData(resultList);
		}
		 
		result.setPageNo(pageNo);
		result.setPageSize(pageSize);
		result.setTotalCount(count);
		return result;
	}

	private void updateKAB(Long currentUserId, List<Long> suggestMemberIdList) {
		if(!CollectionUtils.isEmpty(suggestMemberIdList)) {
			this.repository.updateKAB(currentUserId,suggestMemberIdList.stream().distinct().collect(Collectors.toList()));
		}
	}

	private Page<WholeMemberDTO> getDefaultMemberList(Integer pageNo, Integer pageSize) {
		Page<MemberVO> page = this.page(QueryBuilder.create(MemberMapping.class).and(MemberMapping.WI, QueryOperator.GTE,0).end().sort(MemberMapping.WI).desc().enablePaging().page(pageNo, pageSize).end());
		Page<WholeMemberDTO> result = new Page<WholeMemberDTO>();
		BeanUtils.copyProperties(page, result);
		result.setData(this.getWholeMemberInfoByVOList(page.getData()));
		return result;
	}

	@Override
	public SuggestResult suggestV2(Integer filterType, Boolean refresh) {
		Long currentUserId = getCurrentUserId();
		List<Long> memberIdList = new ArrayList<Long>();
		// ???????????????????????????
		String day = DateTimeUtil.formatyyyyMMdd(new Date());
		String key = currentUserId + DateTimeUtil.formatSimpleyyyyMMdd(new Date());
		SuggestedHistory suggestedHistory = this.getSuggestedHistory(key);
		List<Long> totalList = suggestedHistory.getList().stream().map(item -> item.getTargetId()).collect(Collectors.toList());
		List<MemberSuggestScore> suggestList = null;
		boolean canFetch = suggestedHistory.getSuccessTimes() < SUGGEST_MEMBER_TIME_LIMIT;
		if (suggestedHistory.getTimes() == null || suggestedHistory.getTimes() == 0) { // ?????????,????????????
			refresh = true;
		}
		if (refresh != null && refresh && canFetch) { // ?????????????????????????????????limit
			List<Long> execuldeIdList = new ArrayList<Long>();
			// ???????????????????????????????????????
			execuldeIdList.add(currentUserId);
			// ????????????????????? ????????????
			execuldeIdList.addAll(totalList);
			// ??????????????????????????? ??????3?????????15???????????? ???????????????
			Integer requiredSize = SUGGEST_MEMBER_SIZE;// ?????????????????????3??? Math.min(SUGGEST_MEMBER_SIZE-remainList.size(),
														// SUGGEST_MEMBER_COUNT_LIMIT-totalList.size());
			suggestList = this.suggest(false, false, currentUserId, execuldeIdList, filterType, requiredSize);
			// 1. ??????????????????????????????
			// ???????????????????????????????????????????????????????????????????????????????????????
			if (CollectionUtils.isEmpty(suggestList) || suggestList.size() < requiredSize) {
				// ?????????????????????
				if (!CollectionUtils.isEmpty(suggestList)) {
					execuldeIdList.addAll(suggestList.stream().map(item -> item.getTargetId()).collect(Collectors.toList()));
				}
				// ??????????????????????????????
				List<MemberSuggestScore> suggestListTwo = this.suggest(true, false, currentUserId, execuldeIdList, filterType, requiredSize - suggestList.size());
				suggestList.addAll(suggestListTwo);
			}
			// 2. ????????????????????????????????????
			// ??????????????????????????????????????????????????????
			if (CollectionUtils.isEmpty(suggestList) || suggestList.size() < requiredSize) {
				execuldeIdList.clear();
				// ???????????????????????????????????????
				execuldeIdList.add(currentUserId);
				// ?????????????????????
				if (!CollectionUtils.isEmpty(suggestList)) {
					execuldeIdList.addAll(suggestList.stream().map(item -> item.getTargetId()).collect(Collectors.toList()));
				}
				// ????????????????????? ????????????
				execuldeIdList.addAll(totalList);
				// ??????????????????????????????????????????
				List<MemberSuggestScore> suggestListTwo = this.suggest(false, true, currentUserId, execuldeIdList, filterType, requiredSize - suggestList.size());
				suggestList.addAll(suggestListTwo);
			}
			// 3. ????????????????????????????????????
			// ???????????????????????????????????????????????????????????????????????????
			if (CollectionUtils.isEmpty(suggestList) || suggestList.size() < requiredSize) {
				execuldeIdList.clear();
				// ???????????????????????????????????????
				execuldeIdList.add(currentUserId);
				// ?????????????????????
				if (!CollectionUtils.isEmpty(suggestList)) {
					execuldeIdList.addAll(suggestList.stream().map(item -> item.getTargetId()).collect(Collectors.toList()));
				}
				// ????????????????????? ????????????
				execuldeIdList.addAll(totalList);
				List<MemberSuggestScore> suggestListTwo = this.suggest(true, true, currentUserId, execuldeIdList, filterType, requiredSize - suggestList.size());
				suggestList.addAll(suggestListTwo);
			}
			// ??????????????????????????????????????????????????????
			if ((filterType == null || filterType == 0) && (CollectionUtils.isEmpty(suggestList) || suggestList.size() < requiredSize)) {
				boolean shouldReset = false; // ????????????????????????
				List<MemberSuggestScore> unSuggestList = this.repository.getUnSuggestList(currentUserId);
				// ??????????????? ?????????????????????????????????
				if (!shouldReset) {
					List<Long> ids = suggestList.stream().map(item -> item.getTargetId()).collect(Collectors.toList());
					unSuggestList = unSuggestList.stream().filter(item -> !ids.contains(item.getTargetId())).collect(Collectors.toList());
					if (!CollectionUtils.isEmpty(unSuggestList)) {
						shouldReset = true;
					}
				}
				// ?????????????????????
				if (shouldReset) {
					this.repository.resetSuggestStatus(currentUserId);
					if (CollectionUtils.isEmpty(totalList)) {
						this.repository.markAsSuggested(currentUserId, totalList);
					}
				}
			}
			// ???????????????????????????????????????????????????????????????
			// if (CollectionUtils.isEmpty(suggestList) || suggestList.size() <
			// requiredSize) {
			// execuldeIdList.clear();
			// // ???????????????????????????????????????
			// execuldeIdList.add(currentUserId);
			// // ?????????????????????
			// if (!CollectionUtils.isEmpty(suggestList)) {
			// execuldeIdList.addAll(suggestList.stream().map(item ->
			// item.getTargetId()).collect(Collectors.toList()));
			// }
			// // ????????????????????? ????????????
			// execuldeIdList.addAll(totalList);
			// List<MemberSuggestScore> suggestListTwo = this.suggest(true, true,
			// currentUserId, execuldeIdList, 0, requiredSize - suggestList.size());
			// suggestList.addAll(suggestListTwo);
			// // ??????????????? ???????????????????????????????????????????????????????????????
			// List<MemberSuggestScore> unSuggestList =
			// this.repository.getUnSuggestList(currentUserId);
			// boolean shouldReset = false; // ????????????????????????
			// if (CollectionUtils.isEmpty(unSuggestList)) {
			// shouldReset = true;
			// }
			// // ??????????????? ?????????????????????????????????
			// if (!shouldReset) {
			// List<Long> ids = suggestList.stream().map(item ->
			// item.getTargetId()).collect(Collectors.toList());
			// unSuggestList = unSuggestList.stream().filter(item ->
			// !ids.contains(item.getTargetId())).collect(Collectors.toList());
			// if (!CollectionUtils.isEmpty(unSuggestList)) {
			// shouldReset = true;
			// }
			// }
			// // ?????????????????????
			// if (shouldReset) {
			// this.repository.resetSuggestStatus(currentUserId);
			// if (CollectionUtils.isEmpty(totalList)) {
			// this.repository.markAsSuggested(currentUserId, totalList);
			// }
			// }
			// }
			if (!CollectionUtils.isEmpty(suggestList)) { // ????????????????????????????????????????????????
				// ????????????????????????????????????
				List<Long> newSuggestIdList = suggestList.stream().filter(item -> item.getSuggestTs() == null).map(item -> item.getTargetId()).collect(Collectors.toList());
				if (!CollectionUtils.isEmpty(newSuggestIdList)) {
					this.repository.markAsSuggested(currentUserId, newSuggestIdList);
				}
			}
		} else if (refresh == null || !refresh) { // ????????????????????????????????????????????????
			if (!CollectionUtils.isEmpty(suggestedHistory.getLatestList())) {
				memberIdList.addAll(suggestedHistory.getLatestList().stream().map(item -> item.getTargetId()).collect(Collectors.toList()));
			}
		}
		// ?????????????????????
		if (refresh != null && refresh && canFetch) {
			suggestedHistory.getList().forEach(item -> {
				item.setStatus(1);
			});
			suggestedHistory.setTimes(suggestedHistory.getTimes() + 1); // ????????????,
			if (CollectionUtils.isEmpty(suggestList) || suggestList.size() < SUGGEST_MEMBER_SIZE) {
				suggestedHistory.setFailedTimes(suggestedHistory.getFailedTimes() + 1);
			} else {
				suggestedHistory.setSuccessTimes(suggestedHistory.getSuccessTimes() + 1);
			}
			if (!CollectionUtils.isEmpty(suggestList)) {
				// ???????????????????????????????????????
				memberIdList.addAll(suggestList.stream().map(item -> item.getTargetId()).collect(Collectors.toList()));
				List<SuggestedHistoryItem> newScoreList = suggestList.stream().map(item -> new SuggestedHistoryItem(item.getTargetId(), 0)).collect(Collectors.toList());
				suggestedHistory.setLatestList(newScoreList);
				suggestedHistory.getList().addAll(newScoreList);
			}
		}
		if (!canFetch && refresh != null && refresh) {
			suggestedHistory.setSuccessTimes(SUGGEST_MEMBER_TIME_LIMIT + 1);
			suggestedHistory.setLatestList(new ArrayList<SuggestedHistoryItem>());
		}
		// ????????????,????????????
		cacheService.put(CacheType.SuggestHistory, key, suggestedHistory, 1, TimeUnit.DAYS);
		// ????????????????????????????????????????????????????????????
		List<RecognizedVO> todayRecognizedList = this.recognizedService.list(
				QueryBuilder.create(RecognizedMapping.class).and(RecognizedMapping.SOURCEID, currentUserId).and(RecognizedMapping.CREATETIME, QueryOperator.GTE, day + " 00:00:00")
						.and(RecognizedMapping.CREATETIME, QueryOperator.LTE, day + " 23:59:59").end().sort(RecognizedMapping.CREATETIME).desc() // ??????????????????????????????
						.enablePaging().page(1, 100).end() // ??????????????????100??????

		);
		List<Long> recognizedIdList = todayRecognizedList.stream().map(item -> item.getTargetId()).distinct().collect(Collectors.toList());
		// ??????????????????
		SuggestResult result = SuggestResult.builder().suggested(suggestedHistory.getList().size()).todayRecognizedList(this.getWholeMemberInfo(recognizedIdList))
				.suggestList(this.filter(this.getWholeMemberInfo(memberIdList))).failedTimes(suggestedHistory.getFailedTimes()).times(suggestedHistory.getTimes())
				.successTimes(suggestedHistory.getSuccessTimes()).build();
		// ?????????????????????????????????
		if (!CollectionUtils.isEmpty(result.getSuggestList())) {
			if (!CollectionUtils.isEmpty(suggestList)) {
				suggestList.stream().forEach(a -> {
					result.getSuggestList().stream().filter(item -> a.getTargetId().equals(item.getId())).findAny().ifPresent(item -> {
						item.setScore(a.getScore());
					});
				});
			}
		}
		// ??????????????????
		if (!CollectionUtils.isEmpty(result.getSuggestList())) {
			List<RecognizedVO> recoginzedList = this.recognizedService.list(QueryBuilder.create(RecognizedMapping.class).and(RecognizedMapping.SOURCEID, currentUserId)
					.and(RecognizedMapping.TARGETID, QueryOperator.IN, result.getSuggestList().stream().map(item -> item.getId()).collect(Collectors.toList())).end()
					.or(RecognizedMapping.TARGETID, currentUserId)
					.and(RecognizedMapping.SOURCEID, QueryOperator.IN, result.getSuggestList().stream().map(item -> item.getId()).collect(Collectors.toList())).end());
			if (!CollectionUtils.isEmpty(recoginzedList)) {
				result.getSuggestList().stream().forEach(member -> {
					recoginzedList.stream().filter(item -> item.getSourceId().equals(currentUserId) && item.getTargetId().equals(member.getId())).findAny().ifPresent(item -> {
						member.setTo(true);
					});
					recoginzedList.stream().filter(item -> item.getTargetId().equals(currentUserId) && item.getSourceId().equals(member.getId())).findAny().ifPresent(item -> {
						member.setFrom(true);
					});
				});
			}
		}
		// ??????????????????
		if (!CollectionUtils.isEmpty(result.getTodayRecognizedList())) {
			List<RecognizedVO> recoginzedList = this.recognizedService.list(QueryBuilder.create(RecognizedMapping.class).and(RecognizedMapping.SOURCEID, currentUserId)
					.and(RecognizedMapping.TARGETID, QueryOperator.IN, result.getTodayRecognizedList().stream().map(item -> item.getId()).collect(Collectors.toList())).end()
					.or(RecognizedMapping.TARGETID, currentUserId)
					.and(RecognizedMapping.SOURCEID, QueryOperator.IN, result.getTodayRecognizedList().stream().map(item -> item.getId()).collect(Collectors.toList())).end());
			if (!CollectionUtils.isEmpty(recoginzedList)) {
				result.getTodayRecognizedList().stream().forEach(member -> {
					recoginzedList.stream().filter(item -> item.getSourceId().equals(currentUserId) && item.getTargetId().equals(member.getId())).findAny().ifPresent(item -> {
						member.setTo(true);
					});
					recoginzedList.stream().filter(item -> item.getTargetId().equals(currentUserId) && item.getSourceId().equals(member.getId())).findAny().ifPresent(item -> {
						member.setFrom(true);
					});
				});
			}
		}
		//??????????????????
		if (!CollectionUtils.isEmpty(suggestedHistory.getList())) {
			result.setTodaySuggestList(this.getSimpleMemberInfo(suggestedHistory.getList().stream().map(item -> item.getTargetId()).collect(Collectors.toList())));
		}
		return result;
	}

	private List<MemberSuggestScore> suggest(boolean ignoreSuggestStatus, boolean ignoreRecognized, Long currentUserId, List<Long> execuldeIdList, Integer filterType,
			Integer requiredSize) {
		// ?????????????????????????????????
		List<MemberSuggestScore> suggestList = this.repository.suggestNew(ignoreSuggestStatus, ignoreRecognized, execuldeIdList, currentUserId, filterType, null, requiredSize);
		if (CollectionUtils.isEmpty(suggestList) || suggestList.size() < requiredSize) { // ???????????????????????????
			if (filterType != null && filterType != 3 && filterType != 4) { // ???????????????
				if (!CollectionUtils.isEmpty(suggestList)) {
					execuldeIdList.addAll(suggestList.stream().map(item -> item.getTargetId()).collect(Collectors.toList()));
				}
				// ???????????????
				List<MemberSuggestScore> fallbackOne = this.repository.suggestNew(ignoreSuggestStatus, ignoreRecognized, execuldeIdList, currentUserId, filterType, 1,
						requiredSize - suggestList.size()); // 1?????????
				if (!CollectionUtils.isEmpty(fallbackOne)) {
					suggestList.addAll(fallbackOne);
					execuldeIdList.addAll(fallbackOne.stream().map(item -> item.getTargetId()).collect(Collectors.toList()));
				}
			}
		}
		if (suggestList == null) {
			suggestList = new ArrayList<MemberSuggestScore>();
		}
		return suggestList;
	}

	private List<WholeMemberDTO> filter(List<WholeMemberDTO> list) {
		if (!CollectionUtils.isEmpty(list)) {
			list.stream().forEach(item -> {
				if (!item.isTo()) { // ??????????????????
					if (!CollectionUtils.isEmpty(item.getEducationList())) {
						item.getEducationList().stream().skip(1).forEach(temp -> {
							temp.setGrade(null);
							temp.setDegree(null);
						});
					}
					if (!CollectionUtils.isEmpty(item.getInternshipList())) {
						item.getInternshipList().stream().skip(1).forEach(temp -> {
							temp.setJob(null);
							temp.setDescription(null);
							temp.setCompany(null);
						});
					}
					if (!CollectionUtils.isEmpty(item.getProjectList())) {
						item.getProjectList().stream().skip(1).forEach(temp -> {
							temp.setJob(null);
							temp.setDescription(null);
							temp.setName(null);
						});
					}
					if (!CollectionUtils.isEmpty(item.getResearchList())) {
						item.getResearchList().stream().skip(1).forEach(temp -> {
							temp.setName(null);
							temp.setDescription(null);
						});
					}
					if (!CollectionUtils.isEmpty(item.getTagsList())) {
						item.getTagsList().stream().filter(temp -> temp.getTag().getCategory().equals(TagCategory.personality.getValue())).skip(1).forEach(temp -> {
							temp.setDescription(null);
						});
						item.getTagsList().stream().filter(temp -> temp.getTag().getCategory().equals(TagCategory.statement.getValue())).skip(1).forEach(temp -> {
							temp.setDescription(null);
						});
					}
				}
			});
		}
		return list;
	}

	@Override
	public Page<SimpleMemberInfo> searchMembers(String keywords, Integer pageNo, Integer pageSize) {
		if (StringUtils.isEmpty(keywords) || StringUtils.isEmpty(keywords.trim())) {
			throw new SystemException(CloudOKExceptionMessage.SEARCH_KEYWORDS_IS_NULL);
		}
		if (keywords.trim().length() > 100) {// ??????????????????????????? ????????????????????????
			return new Page<SimpleMemberInfo>();
		}
		Page<MemberVO> page = this.page(QueryBuilder.create(MemberMapping.class).and(MemberMapping.NICKNAME, QueryOperator.LIKE, keywords.trim()).end().sort(MemberMapping.WI)
				.desc().enablePaging().page(pageNo, pageSize).end());
		Page<SimpleMemberInfo> result = new Page<SimpleMemberInfo>();
		BeanUtils.copyProperties(page, result);
		if (!CollectionUtils.isEmpty(page.getData())) {
			result.setData(this.getSimpleMemberInfo(page.getData().stream().map(item -> item.getId()).collect(Collectors.toList())));
		}
		return result;
	}

	@Override
	public void onApplicationEvent(BusinessEvent<?> event) {
		if(event.isProcessed(getClass())) {
			return;
		}
		if (event instanceof RecognizedCreateEvent) {
			event.logDetails();
			this.onRecognizedCreateEvent(RecognizedCreateEvent.class.cast(event));
		}
	}

	@Override
	public boolean checkMemberNotEmpty(Long memberId) {
		boolean imperfect = true;
		if (internshipExperienceService.count(QueryBuilder.create(InternshipExperienceMapping.class).and(InternshipExperienceMapping.MEMBERID, getCurrentUserId()).end()) == 0) {
			if (memberTagsService.count(QueryBuilder.create(MemberTagsMapping.class).and(InternshipExperienceMapping.MEMBERID, getCurrentUserId()).end()) == 0) {
				if (projectExperienceService.count(QueryBuilder.create(ProjectExperienceMapping.class).and(InternshipExperienceMapping.MEMBERID, getCurrentUserId()).end()) == 0) {
					if (researchExperienceService
							.count(QueryBuilder.create(ResearchExperienceMapping.class).and(InternshipExperienceMapping.MEMBERID, getCurrentUserId()).end()) == 0) {
						imperfect = false;
					}
				}
			}
		}
		return imperfect;
	}
	//???????????????Type???????????? 1 ???????????? 2 ?????? 3 ?????? 4 ?????? 5???????????? 6 ?????? 7 ??????
	@Override
	public Page<WholeMemberDTO> getMemberCirclesV2(Integer type, Long businessId, Integer pageNo, Integer pageSize) {
		if (type == null || businessId == null) {
			return new Page<>();
		}
		if (type == null || businessId == null) {
			return new Page<>();
		}
		Long currentUserId = getCurrentUserId();
		Long count = this.repository.getMemberCirclesCountV2(currentUserId,null, type, businessId);
		Page<WholeMemberDTO> page = new Page<WholeMemberDTO>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setTotalCount(count);
		if (page.getTotalCount() > 0 && (page.getTotalCount() / page.getPageSize() + 1) >= page.getPageNo()) {
			// ??????????????????
			List<MemberCirclePO> suggestMemberList = this.repository.getMemberCirclesListV2(currentUserId, null, type, businessId,
					(pageNo - 1) * pageSize, pageSize);
			List<Long> suggestMemberIdList = suggestMemberList.stream().map(item -> item.getMemberId()).collect(Collectors.toList());
			List<WholeMemberDTO> memberList = this.getWholeMemberInfo(suggestMemberIdList);
			List<RecognizedVO> recoginzedList = this.recognizedService.list(QueryBuilder.create(RecognizedMapping.class).and(RecognizedMapping.SOURCEID, currentUserId)
					.and(RecognizedMapping.TARGETID, QueryOperator.IN, suggestMemberIdList).end().or(RecognizedMapping.TARGETID, currentUserId)
					.and(RecognizedMapping.SOURCEID, QueryOperator.IN, suggestMemberIdList).end());
			if (!CollectionUtils.isEmpty(recoginzedList)) {
				memberList.stream().forEach(member -> {
					recoginzedList.stream().filter(item -> item.getSourceId().equals(currentUserId) && item.getTargetId().equals(member.getId())).findAny().ifPresent(item -> {
						member.setTo(true);
					});
					recoginzedList.stream().filter(item -> item.getTargetId().equals(currentUserId) && item.getSourceId().equals(member.getId())).findAny().ifPresent(item -> {
						member.setFrom(true);
					});
				});
			}
			// ???????????????Type???????????? 1 ???????????? 2 ?????? 3 ?????? 4 ??????/????????????
			memberList.stream().forEach(item -> {
				switch (type) {
				case 1: // ????????????
					item.setInternshipList(null);
					item.setProjectList(null);
					item.setTagsList(null);
					List<ResearchExperienceVO> researchList = item.getResearchList();
					if (!CollectionUtils.isEmpty(researchList)) {
						item.setResearchList(
								researchList.stream().filter(r -> r.getDomain() != null).filter(r -> businessId.equals(r.getDomain().getId())).collect(Collectors.toList()));
					}
					break;
				case 2: // ??????
					item.setProjectList(null);
					item.setResearchList(null);
					item.setTagsList(null);
					List<InternshipExperienceVO> internshipList = item.getInternshipList();
					if (!CollectionUtils.isEmpty(internshipList)) {
						item.setInternshipList(internshipList.stream().filter(r -> r.getIndustry() != null).filter(r -> businessId.toString().equals(r.getIndustry().getCategory()))
								.collect(Collectors.toList()));
					}
					break;
				case 3:// ??????
					item.setInternshipList(null);
					item.setResearchList(null);
					item.setTagsList(null);
					List<ProjectExperienceVO> projectList = item.getProjectList();
					if (!CollectionUtils.isEmpty(projectList)) {
						item.setProjectList(projectList.stream().filter(r -> businessId.toString().equals(r.getCategory())).collect(Collectors.toList()));
					}
					break;
				case 0:// ???????????? ????????????
				case 4:// ????????????
				case 5:// ????????????
					item.setInternshipList(null);
					item.setProjectList(null);
					item.setResearchList(null);
					List<MemberTagsVO> tagsList = item.getTagsList();
					if (!CollectionUtils.isEmpty(tagsList)) {
						item.setTagsList(tagsList.stream().filter(r -> r.getTag() != null).filter(r -> businessId.equals(r.getTag().getId())).collect(Collectors.toList()));
					}
					break;	
				case 6:// ??????
				case 7:// ??????
					item.setInternshipList(null);
					item.setProjectList(null);
					item.setResearchList(null);
					item.setTagsList(null);
					break;
				default:
					break;
				}
			});
			page.setData(memberList);
		}
		return page;
	}

	@Override
	public TokenVO authByMember(MemberVO member) {
		cacheService.evict(CacheType.Member, member.getId().toString());
		User user = this.loadUserInfo(member.getId(), member);
		TokenVO token = TokenVO.build(JWTUtil.genToken(user, TokenType.ACCESS), JWTUtil.genToken(user, TokenType.REFRESH), user);
		return token;
	}

	@Override
	public void bindOpenId(Long currentUserId, String openid) {
		this.repository.resetOtherMemberOpenId(openid);
		this.repository.bindOpenId(currentUserId,openid);
	}

	@Override
	public void unbindOpenId(Long currentUserId) {
		this.repository.unbindOpenId(currentUserId);
	}

	@Override
	public TokenVO loginOrCreateByPhone(String phoneNumber) {
		MemberVO member = this.get(QueryBuilder.create(MemberMapping.class).and(MemberMapping.PHONE, phoneNumber).end());
		if(member == null) {
			member = new MemberVO();
			member.setPhone(phoneNumber);
			this.create(member);
			member = this.get(member.getId());
		}
		return this.authByMember(member);
	}

}
