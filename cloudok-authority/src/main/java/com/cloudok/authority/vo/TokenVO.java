package com.cloudok.authority.vo;

import java.io.Serializable;

import com.cloudok.security.User;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhijian.xia@foxmail.com
 * @date 2020年6月18日 下午12:15:00
 */
@Getter @Setter
public class TokenVO implements Serializable{

	private static final long serialVersionUID = -3648822002510109619L;

	private String accessToken;

	private String refreshToken;

	private User userInfo;
	
	public static TokenVO build(String accessToken,String refreshToken,User user) {
		TokenVO a=new TokenVO();
		a.accessToken=accessToken;
		a.refreshToken=refreshToken;
		a.userInfo=user;
		return a;
	}
}
