package com.cloudok.authority.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudok.core.service.AbstractService;
import com.cloudok.authority.mapper.UserMapper;
import com.cloudok.authority.po.UserPO;
import com.cloudok.authority.service.UserService;
import com.cloudok.authority.vo.UserVO;

@Service
public class UserServiceImpl extends AbstractService<UserVO, UserPO> implements UserService{

	@Autowired
	public UserServiceImpl(UserMapper repository) {
		super(repository);
	}
}
