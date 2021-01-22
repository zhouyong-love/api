package com.cloudok.security;


import com.cloudok.core.enums.UserType;
import com.cloudok.security.token.JWTTokenInfo;

/**
 * @author zhijian.xia@foxmail.com
 * @date 2020年6月29日 下午6:39:53
 */
public interface UserInfoHandler {

	User loadUserInfoByToken(JWTTokenInfo info);
	
	UserType getUserType();
	
	
}
