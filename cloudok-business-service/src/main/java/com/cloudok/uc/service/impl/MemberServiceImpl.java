package com.cloudok.uc.service.impl;

import java.util.ArrayList;
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
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.cloudok.base.message.utils.MessageUtil;
import com.cloudok.base.message.vo.MessageReceive;
import com.cloudok.cache.Cache;
import com.cloudok.common.CacheType;
import com.cloudok.common.Constants;
import com.cloudok.core.enums.UserType;
import com.cloudok.core.exception.CoreExceptionMessage;
import com.cloudok.core.exception.SystemException;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.query.QueryOperator;
import com.cloudok.core.service.AbstractService;
import com.cloudok.exception.CloudOKExceptionMessage;
import com.cloudok.security.SecurityContextHelper;
import com.cloudok.security.User;
import com.cloudok.security.UserInfoHandler;
import com.cloudok.security.exception.SecurityExceptionMessage;
import com.cloudok.security.token.JWTTokenInfo;
import com.cloudok.security.token.JWTUtil;
import com.cloudok.security.token.TokenType;
import com.cloudok.uc.dto.WholeMemberDTO;
import com.cloudok.uc.mapper.MemberMapper;
import com.cloudok.uc.mapping.EducationExperienceMapping;
import com.cloudok.uc.mapping.InternshipExperienceMapping;
import com.cloudok.uc.mapping.MemberMapping;
import com.cloudok.uc.mapping.MemberTagsMapping;
import com.cloudok.uc.mapping.ProjectExperienceMapping;
import com.cloudok.uc.mapping.RecognizedMapping;
import com.cloudok.uc.mapping.ResearchExperienceMapping;
import com.cloudok.uc.po.MemberPO;
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
import com.cloudok.uc.vo.LoginVO;
import com.cloudok.uc.vo.MemberTagsVO;
import com.cloudok.uc.vo.MemberVO;
import com.cloudok.uc.vo.ProjectExperienceVO;
import com.cloudok.uc.vo.RecognizedVO;
import com.cloudok.uc.vo.ResearchExperienceVO;
import com.cloudok.uc.vo.SingupVO;
import com.cloudok.uc.vo.TokenVO;
import com.cloudok.uc.vo.UserCheckRequest;
import com.cloudok.uc.vo.VerifyCodeRequest;

@Service
public class MemberServiceImpl extends AbstractService<MemberVO, MemberPO> implements MemberService,UserInfoHandler{

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private Cache cacheService;
	
	@Autowired
	public MemberServiceImpl(MemberMapper repository) {
		super(repository);
	}

	@Override
	public TokenVO login(LoginVO vo) {
		List<MemberVO> memberList = this.list(QueryBuilder.create(MemberMapping.class)
				.and(MemberMapping.EMAIL, vo.getUserName()).end() //email
				.or(MemberMapping.PHONE,vo.getUserName()) // phone
				.or(MemberMapping.USERNAME,vo.getUserName()) // userName
				.end());
		if (CollectionUtils.isEmpty(memberList)) {
			throw new SystemException(SecurityExceptionMessage.ACCESS_INCORRECT_CERTIFICATE);
		}
		MemberVO sysUser = memberList.get(0);
		if (!passwordEncoder.matches(vo.getPassword(), sysUser.getPassword())) {
			throw new SystemException(SecurityExceptionMessage.ACCESS_INCORRECT_CERTIFICATE);
		}
//		if (sysUser.getFreeze().equals(Boolean.TRUE)) {
//			throw new SystemException(SecurityExceptionMessage.ACCESS_ACCOUNT_FROZEN);
//		}
		cacheService.evict(CacheType.Member, sysUser.getId().toString());
		User user = this.loadUserInfo(sysUser.getId());
		TokenVO token = TokenVO.build(JWTUtil.genToken(user, TokenType.ACCESS),
				JWTUtil.genToken(user, TokenType.REFRESH), user);
		return token;
	}

	private User loadUserInfo(Long userId) {
		User user = cacheService.get(CacheType.Member, userId.toString(),User.class);
		if(user != null) {
			return user;
		}
		MemberVO sysUser = this.get(userId);
		 user = new User();
		BeanUtils.copyProperties(sysUser, user);
		user.setUsername(sysUser.getUserName());
		user.setFullName(sysUser.getNickName());
		user.setUserType(UserType.MEMBER.getType());
		fillAuthorities(user);
		cacheService.put(CacheType.Member, userId.toString(),user);
		return user;
	}

	private void fillAuthorities(User user) {
		List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(Constants.DEFAULT_MEMBER_ROLE));
		user.setAuthorities(authorities);
	}
	@Override
	public Boolean logout() {
		User user =  SecurityContextHelper.getCurrentUser();
		cacheService.evict(CacheType.Member, user.getId().toString());
		return true;
	}

	@Override
	public TokenVO refreshToken(String refreshToken) {
		JWTTokenInfo info = JWTUtil.decodeToken(refreshToken);
		if (!(info.getTokenType().getType()==TokenType.REFRESH.getType())) {
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
		if(isSms) {
			vo.setEmail(null);
		}else {
			vo.setPhone(null);
		}
		String cacheKey =buildKey("forgot",isSms ? "sms":"email",isSms ? vo.getPhone() : vo.getEmail());
		String code = cacheService.get(CacheType.VerifyCode, cacheKey,String.class);
		if(StringUtils.isEmpty(code)) {
			throw new SystemException("verify code is wrong",CloudOKExceptionMessage.VERIFY_CODE_WRONG);
		}
		if(!code.equals(vo.getCode())) {
			throw new SystemException("verify code is wrong",CloudOKExceptionMessage.VERIFY_CODE_WRONG);
		}
		
		List<MemberVO> userList = null;
		if("1".equalsIgnoreCase(vo.getForgotType())) {
			userList = this.list(QueryBuilder.create(MemberMapping.class).and(MemberMapping.EMAIL, vo.getEmail()).end());
			if(CollectionUtils.isEmpty(userList)) {
				throw new SystemException("email not fonud",CoreExceptionMessage.NOTFOUND_ERR);
			}
		}else if("0".equalsIgnoreCase(vo.getForgotType())) {
			userList = this.list(QueryBuilder.create(MemberMapping.class).and(MemberMapping.PHONE, vo.getPhone()).end());
			if(CollectionUtils.isEmpty(userList)) {
				throw new SystemException("phone not found",CoreExceptionMessage.NOTFOUND_ERR);
			}
		}
		MemberVO user  = userList.get(0);
		user.setPassword(passwordEncoder.encode(vo.getPassword()));
		this.merge(user);
		
		return true;
	}
	
	private String buildKey(String module,String type,String key) {
		return module+":"+type+":"+key;
	}
	
	private boolean isEduEmail(String email) {
		if(StringUtils.isEmpty(email)) {
			return false;
		}
		return email.toLowerCase().endsWith("edu");
	}
	@Override
	public TokenVO signup(SingupVO vo) {
		boolean isSms =  "0".equalsIgnoreCase(vo.getRegisterType());
		if(StringUtils.isEmpty(vo.getPhone()) && !StringUtils.isEmpty(vo.getEmail())) { //fixed data, front-end may be error
			if(!isEduEmail(vo.getEmail())) {
				throw new SystemException(CloudOKExceptionMessage.INVALID_EMAIL_ADDRESS);
			}
			vo.setRegisterType("1"); //force set to email
			isSms = false;
			boolean isExits = checkEmailExists(vo.getEmail());
			if(isExits) {
				throw new SystemException(CloudOKExceptionMessage.EMAIL_ALREADY_EXISTS);
			}
		}
		if(!StringUtils.isEmpty(vo.getPhone()) && StringUtils.isEmpty(vo.getEmail())) { //fixed data, front-end may be error
			vo.setRegisterType("0"); //force set to phone
			isSms = true;
			boolean  isExits = checkPhoneExists(vo.getPhone());
			if(isExits) {
				throw new SystemException(CloudOKExceptionMessage.PHONE_ALREADY_EXISTS);
			}
		}
		if(isSms) {
			vo.setEmail(null);
		}else {
			vo.setPhone(null);
		}
		String cacheKey = buildKey("signup",isSms ? "sms":"email",isSms ? vo.getPhone() : vo.getEmail());
		String code = cacheService.get(CacheType.VerifyCode, cacheKey,String.class);
		if(StringUtils.isEmpty(code)) {
			throw new SystemException("verify code is wrong",CloudOKExceptionMessage.VERIFY_CODE_WRONG);
		}
		if(!code.equals(vo.getCode())) {
			throw new SystemException("verify code is wrong",CloudOKExceptionMessage.VERIFY_CODE_WRONG);
		}
		 
		MemberVO member = new MemberVO();
		member.setEmail(vo.getEmail());
		member.setPhone(vo.getPhone());
		this.create(member);
		 
		MemberVO sysUser = this.get(member.getId());
		cacheService.evict(CacheType.Member, sysUser.getId().toString());
		User user = this.loadUserInfo(sysUser.getId());
		TokenVO token = TokenVO.build(JWTUtil.genToken(user, TokenType.ACCESS), JWTUtil.genToken(user, TokenType.REFRESH), user);
		
		return token;
	}
	
	@Override
	public MemberVO fillAccountInfo(@Valid MemberVO vo) {
		MemberVO member = new MemberVO();
		boolean isExits = checkUserNameExists(vo.getUserName());
		if(isExits) {
			throw new SystemException(CloudOKExceptionMessage.USERNAME_ALREADY_EXISTS);
		}
		member.setId(SecurityContextHelper.getCurrentUserId());
		member.setNickName(vo.getNickName());
		member.setAvatar(vo.getAvatar());
		member.setUserName(vo.getUserName());
		member.setPassword(passwordEncoder.encode(vo.getPassword()));
		this.merge(member);
		return this.get(member.getId());
	}

	@Override
	public Boolean sendVerifycode(VerifyCodeRequest vo) {
		String cacheKey =buildKey(vo.getModule(),vo.getType(),vo.getKey());
		if(cacheService.exist(CacheType.VerifyCode, "S:"+cacheKey)) { //防止重复发送
			return null;
		}
		String count = cacheService.get(CacheType.VerifyCodeCount, cacheKey,String.class);
        int _c = count == null ? 0 : Integer.parseInt(count);
        if (_c <= 10) { //每小时最多10次
        	throw new SystemException(CloudOKExceptionMessage.TOO_MANY_VERIFY_CODE);
        }
        
		String code = RandomStringUtils.random(6, false, true);
		int checkType = 0;
		switch (vo.getModule().toLowerCase()) {
			case "signup":
				//check unique
				checkType = 1;
				break;
			case "forgot":
				//check exists
				checkType = 2;
				break;
			case "bind":
				//check unique
				checkType = 1;
				break;
			default:
				throw new SystemException(CloudOKExceptionMessage.UNKNOW_VERIFY_CODE_TYPE);
		}
		if("sms".equalsIgnoreCase(vo.getType())) {
			boolean  exists =this.checkPhoneExists(vo.getKey());
			if(checkType == 1 && exists) {
				if(vo.getModule().toLowerCase().equals("signup")) {
					throw new SystemException(CloudOKExceptionMessage.PHONE_ALREADY_EXISTS);
				} 
				if(vo.getModule().toLowerCase().equals("bind")) {
					throw new SystemException("Phone number is bound",CloudOKExceptionMessage.PHONE_ALREADY_EXISTS);
				} 
				
			}
			if(checkType == 2 && !exists) {
				throw new SystemException(CloudOKExceptionMessage.PHONE_NOT_FOUND);
			}
		}else if("email".equalsIgnoreCase(vo.getType())) {
			if(!isEduEmail(vo.getKey())) {
				throw new SystemException(CloudOKExceptionMessage.INVALID_EMAIL_ADDRESS);
			}
			boolean  exists =this.checkEmailExists(vo.getKey());
			if(checkType == 1 && exists) {
				if(vo.getModule().toLowerCase().equals("signup")) {
					throw new SystemException(CloudOKExceptionMessage.EMAIL_ALREADY_EXISTS);
				} 
				if(vo.getModule().toLowerCase().equals("bind")) {
					throw new SystemException("Email is bound",CloudOKExceptionMessage.EMAIL_ALREADY_EXISTS);
				} 
				
			}
			if(checkType == 2 && !exists) {
				throw new SystemException(CloudOKExceptionMessage.EMAIL_NOT_FOUND);
			}
		}else {
			throw new SystemException("type should be email or sms",CloudOKExceptionMessage.VERIFY_CODE_SEND_ERROR);
		}
		 Map<String, String> params = new HashMap<String, String>();
         params.put("code", code);
		MessageUtil.send(vo.getModule()+"_"+vo.getType(), params, new MessageReceive.DirectMessageReceive(vo.getKey()));
		 
		//code 10 minutes
		cacheService.put(CacheType.VerifyCode, cacheKey, code, 12, TimeUnit.MINUTES);
		cacheService.put(CacheType.VerifyCode, "S:"+cacheKey, code, 30, TimeUnit.SECONDS);
		cacheService.put(CacheType.VerifyCodeCount, cacheKey, _c+1, 1, TimeUnit.HOURS);
		return true;
	}

	private boolean checkEmailExists(String email) {
		if(!isEduEmail(email)) {
			throw new SystemException(CloudOKExceptionMessage.INVALID_EMAIL_ADDRESS);
		}
		return  !CollectionUtils.isEmpty(this.list(QueryBuilder.create(MemberMapping.class).and(MemberMapping.EMAIL, email).end()));
	}
	
	private boolean checkPhoneExists(String phone) {
		 return  !CollectionUtils.isEmpty(this.list(QueryBuilder.create(MemberMapping.class).and(MemberMapping.PHONE,phone).end()));
	}

	private boolean checkUserNameExists(String userName) {
		 return !CollectionUtils.isEmpty(this.list(QueryBuilder.create(MemberMapping.class).and(MemberMapping.USERNAME,userName).end()));
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
			 throw new SystemException("Current password is wrong, please retry",CloudOKExceptionMessage.DEFAULT_ERROR);
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
		String cacheKey =buildKey("bind",request.getType(),request.getKey());
		String code = cacheService.get(CacheType.VerifyCode, cacheKey,String.class);
		if(StringUtils.isEmpty(code)) {
			throw new SystemException("verify code is wrong",CloudOKExceptionMessage.VERIFY_CODE_WRONG);
		}
		if(!code.equals(request.getCode())) {
			throw new SystemException("verify code is wrong",CloudOKExceptionMessage.VERIFY_CODE_WRONG);
		}
		if("email".equalsIgnoreCase(request.getType())) {
			MemberVO user = new MemberVO();
			user.setEmail(request.getKey());
			user.setId(vo.getId());
			this.merge(user);
		}else if("sms".equalsIgnoreCase(request.getType())) {
			MemberVO user = new MemberVO();
			user.setPhone(request.getKey());
			user.setId(vo.getId());
			this.merge(user);
		}
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
	
	
	private EducationExperienceService educationExperienceService;
	private InternshipExperienceService internshipExperienceService;
	private MemberTagsService memberTagsService;
	private ProjectExperienceService projectExperienceService;
	private RecognizedService recognizedService;
	private ResearchExperienceService researchExperienceService;
	
	
	@Override
	public List<WholeMemberDTO> getWholeMemberInfo(List<Long> memberIdList) {
		memberIdList = memberIdList.stream().distinct().collect(Collectors.toList());
		List<WholeMemberDTO> memberList = this.get(memberIdList).stream().map(item ->{
			WholeMemberDTO dto = new WholeMemberDTO();
			BeanUtils.copyProperties(item, dto);
			return dto;
		}).collect(Collectors.toList());
		
		if(!CollectionUtils.isEmpty(memberList)) {
			educationExperienceService.list(QueryBuilder.create(EducationExperienceMapping.class).and(EducationExperienceMapping.MEMBERID, QueryOperator.IN,memberIdList).end())
			.stream().collect(Collectors.groupingBy(EducationExperienceVO::getMemberId))
			.forEach((memberId,valueList)->{
				memberList.stream().filter(item -> item.getId().equals(memberId)).findAny().ifPresent(item -> {
					item.setEducationList(valueList);
				});
			});
			
			internshipExperienceService.list(QueryBuilder.create(EducationExperienceMapping.class).and(InternshipExperienceMapping.MEMBERID, QueryOperator.IN,memberIdList).end())
			.stream().collect(Collectors.groupingBy(InternshipExperienceVO::getMemberId))
			.forEach((memberId,valueList)->{
				memberList.stream().filter(item -> item.getId().equals(memberId)).findAny().ifPresent(item -> {
					item.setInternshipList(valueList);
				});
			});
			
			memberTagsService.list(QueryBuilder.create(EducationExperienceMapping.class).and(MemberTagsMapping.MEMBERID, QueryOperator.IN,memberIdList).end())
			.stream().collect(Collectors.groupingBy(MemberTagsVO::getMemberId))
			.forEach((memberId,valueList)->{
				memberList.stream().filter(item -> item.getId().equals(memberId)).findAny().ifPresent(item -> {
					item.setTagsList(valueList);
				});
			});
			
			projectExperienceService.list(QueryBuilder.create(EducationExperienceMapping.class).and(ProjectExperienceMapping.MEMBERID, QueryOperator.IN,memberIdList).end())
			.stream().collect(Collectors.groupingBy(ProjectExperienceVO::getMemberId))
			.forEach((memberId,valueList)->{
				memberList.stream().filter(item -> item.getId().equals(memberId)).findAny().ifPresent(item -> {
					item.setProjectList(valueList);
				});
			});
			

			researchExperienceService.list(QueryBuilder.create(EducationExperienceMapping.class).and(ResearchExperienceMapping.MEMBERID, QueryOperator.IN,memberIdList).end())
			.stream().collect(Collectors.groupingBy(ResearchExperienceVO::getMemberId))
			.forEach((memberId,valueList)->{
				memberList.stream().filter(item -> item.getId().equals(memberId)).findAny().ifPresent(item -> {
					item.setResearchList(valueList);
				});
			});
			
			recognizedService.list(QueryBuilder.create(EducationExperienceMapping.class).and(RecognizedMapping.SOURCEID, QueryOperator.IN,memberIdList).end())
			.stream().collect(Collectors.groupingBy(RecognizedVO::getSourceId))
			.forEach((memberId,valueList)->{
				memberList.stream().filter(item -> item.getId().equals(memberId)).findAny().ifPresent(item -> {
					item.setRecognizedMemberList(valueList.stream().map(i -> i.getTargetId()).distinct().collect(Collectors.toList()));
				});
			});
			
			recognizedService.list(QueryBuilder.create(EducationExperienceMapping.class).and(RecognizedMapping.TARGETID, QueryOperator.IN,memberIdList).end())
			.stream().collect(Collectors.groupingBy(RecognizedVO::getSourceId))
			.forEach((memberId,valueList)->{
				memberList.stream().filter(item -> item.getId().equals(memberId)).findAny().ifPresent(item -> {
					item.setRecognizedByList(valueList.stream().map(i -> i.getSourceId()).distinct().collect(Collectors.toList()));
				});
			});
			 
		}
		return memberList;
	}	
	@Override
	public WholeMemberDTO getWholeMemberInfo(Long memberId) {
		List<WholeMemberDTO> list = this.getWholeMemberInfo(java.util.Collections.singletonList(memberId));
		return CollectionUtils.isEmpty(list) ? null : list.get(0);
	}
}
