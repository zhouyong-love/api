package com.cloudok.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextHelper {

	/**
	 * 获取登陆用户id
	 * @return
	 */
	public static Long getCurrentUserId() {
		User user = getCurrentUser();
		if (user != null) {
			return user.getId();
		}
		return null;
	}

	/**
	 * 获取登陆用户名
	 * @return
	 */
	public static String getCurrentUserName() {
		User user = getCurrentUser();
		if (user != null) {
			return user.getUsername();
		}
		return null;
	}
	
	/**
	 * 从Security 获取用户信息
	 * @return
	 */
	public static User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			if(authentication.getPrincipal() instanceof User) {
				return (User) authentication.getPrincipal();
			}
		}
		return null;
	}

	/**
	 * 判断用户是否登陆
	 * @return
	 */
	public static boolean isLogin() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			Object detail = authentication.getPrincipal();
			if (detail instanceof User) {
				return true;
			}
		}
		return false;
	}
}
