package com.cloudok.authority.service;

import com.cloudok.authority.vo.LoginVO;
import com.cloudok.security.User;

/**
 * 登陆适配器
 * @author xiazhijian
 *
 */
public interface LoginAdapter {
	
	/**
	 * 认证
	 * @param vo
	 * @return
	 */
	public User login(LoginVO vo);
}
