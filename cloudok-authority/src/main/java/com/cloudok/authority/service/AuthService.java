package com.cloudok.authority.service;

import com.cloudok.authority.vo.LoginVO;
import com.cloudok.authority.vo.TokenVO;
import com.cloudok.security.User;

public interface AuthService {

	TokenVO login(LoginVO vo);
	
	void logout();
	
	TokenVO refreshToken(String refreshToken);
	
	String provisionalPass();
	
	User userInfo();
	
	User getSessionFromCache(String key) ;
	
	void cacheSession(User user) ;
}
