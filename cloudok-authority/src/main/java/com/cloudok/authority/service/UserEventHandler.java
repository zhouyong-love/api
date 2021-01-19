package com.cloudok.authority.service;

import com.cloudok.authority.vo.UserVO;

/**
 * 用户事件
 * @author xiazhijian
 *
 */
public interface UserEventHandler {

	void addUser(UserVO user);
	
	void updateUser(UserVO user);
	
	void deleteUser(Long userId);
}
