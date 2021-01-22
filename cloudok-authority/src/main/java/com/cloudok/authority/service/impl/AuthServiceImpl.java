package com.cloudok.authority.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.cloudok.authority.enums.ResourceType;
import com.cloudok.authority.service.AuthService;
import com.cloudok.authority.service.LoginAdapter;
import com.cloudok.authority.service.RoleResService;
import com.cloudok.authority.service.UserService;
import com.cloudok.authority.service.grant.RoleGrantAdapter;
import com.cloudok.authority.vo.LoginVO;
import com.cloudok.authority.vo.ResourceVO;
import com.cloudok.authority.vo.RoleVO;
import com.cloudok.authority.vo.TokenVO;
import com.cloudok.authority.vo.UserVO;
import com.cloudok.cache.Cache;
import com.cloudok.cache.CacheNameSpace;
import com.cloudok.core.context.SpringApplicationContext;
import com.cloudok.core.enums.UserType;
import com.cloudok.core.exception.SystemException;
import com.cloudok.security.SecurityContextHelper;
import com.cloudok.security.User;
import com.cloudok.security.UserInfoHandler;
import com.cloudok.security.exception.SecurityExceptionMessage;
import com.cloudok.security.token.JWTTokenInfo;
import com.cloudok.security.token.JWTUtil;
import com.cloudok.security.token.TokenProperties;
import com.cloudok.security.token.TokenType;

@Service
public class AuthServiceImpl implements AuthService, UserInfoHandler {

	public static final CacheNameSpace SESSIONCACHE = new CacheNameSpace("system", "session");

	@Autowired
	private TokenProperties properties;

	@Autowired
	private Cache cache;
	
	@Autowired
	private UserService userService;

	@Override
	public TokenVO login(LoginVO vo) {
		User user = SpringApplicationContext.getBean(vo.getAuthType() + "LoginHandler", LoginAdapter.class).login(vo);
		if(user==null) {
			throw new SystemException(SecurityExceptionMessage.ACCESS_INCORRECT_CERTIFICATE);
		}
		user.setUserType(UserType.SYS_USER.getType());
		user.setPassword(null);
		user.setAuthorities(getAuthorities(user.getId()));
		TokenVO token = TokenVO.build(JWTUtil.genToken(user, TokenType.ACCESS),
				JWTUtil.genToken(user, TokenType.REFRESH),
				user);
		cache.put(SESSIONCACHE, String.valueOf(user.getId()), user);
		return token;
	}
	

	@Autowired
	private RoleGrantAdapter roleGrantAdapter;
	
	@Autowired
	private RoleResService roleResService;
	
	public List<SimpleGrantedAuthority> getAuthorities(Long userId) {
		List<RoleVO> roles = roleGrantAdapter.getUserRoles(userId);
		List<ResourceVO> resources = roleResService.getResourceByRole(roles.stream().map(item->item.getId()).collect(Collectors.toList()));
		List<SimpleGrantedAuthority> authoritys=new ArrayList<>();
		for (RoleVO role : roles) {
			authoritys.add(new SimpleGrantedAuthority("role."+role.getRoleCode()));
		}
		for (ResourceVO resource : resources) {
			authoritys.add(new SimpleGrantedAuthority(ResourceType.parse(resource.getResourceType()).getLabel()+"."+resource.getResourceCode()));
		}
		return authoritys;
	}
	
	@Override
	public void logout() {
		if(SecurityContextHelper.isLogin()) {
			cache.exist(SESSIONCACHE, String.valueOf(SecurityContextHelper.getCurrentUserId()));
		}
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
		cache.expire(SESSIONCACHE, String.valueOf(SecurityContextHelper.getCurrentUserId()), properties.getExpired());
		return token;
	}

	@Override
	public String provisionalPass() {
		return JWTUtil.getProvisionalPass(properties.getSignatureExpired());
	}

	@Override
	public User userInfo() {
		UserVO user = userService.get(SecurityContextHelper.getCurrentUserId());
		User securityUser=new User();
		BeanUtils.copyProperties(user, securityUser);
		securityUser.setUsername(user.getUserName());
		securityUser.setUserType(UserType.SYS_USER.getType());
		return securityUser;
	}

	@Override
	public User loadUserInfoByToken(JWTTokenInfo info) {
		return getSessionFromCache(info.getKey());
	}

	@Override
	public User getSessionFromCache(String key) {
		return cache.get(SESSIONCACHE, key, User.class);
	}

	@Override
	public void cacheSession(User user) {
		cache.put(SESSIONCACHE, String.valueOf(user.getId()), user, properties.getExpired(), TimeUnit.SECONDS);
	}
	
	@Override
	public UserType getUserType() {
		return UserType.SYS_USER;
	}

}
