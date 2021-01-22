package com.cloudok.authority.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cloudok.authority.mapper.UserMapper;
import com.cloudok.authority.mapping.UserMapping;
import com.cloudok.authority.po.UserPO;
import com.cloudok.authority.service.LoginAdapter;
import com.cloudok.authority.service.UserService;
import com.cloudok.authority.vo.LoginVO;
import com.cloudok.authority.vo.UserVO;
import com.cloudok.core.enums.UserType;
import com.cloudok.core.exception.SystemException;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.service.AbstractService;
import com.cloudok.security.User;
import com.cloudok.security.exception.SecurityExceptionMessage;

@Service
public class UserServiceImpl extends AbstractService<UserVO, UserPO> implements UserService,LoginAdapter{

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	public UserServiceImpl(UserMapper repository) {
		super(repository);
	}

	@Override
	public User login(LoginVO vo) {
		List<UserVO> userList = this.list(QueryBuilder.create(UserMapping.class)
				.or(UserMapping.USERNAME,vo.getUserName()) // userName
				.end());
		if (CollectionUtils.isEmpty(userList)) {
			throw new SystemException(SecurityExceptionMessage.ACCESS_INCORRECT_CERTIFICATE);
		}
		UserVO sysUser = userList.get(0);
		if (!passwordEncoder.matches(vo.getPassword(), sysUser.getPassword())) {
			throw new SystemException(SecurityExceptionMessage.ACCESS_INCORRECT_CERTIFICATE);
		}
		if (sysUser.getFreeze().equals(Boolean.TRUE)) {
			throw new SystemException(SecurityExceptionMessage.ACCESS_ACCOUNT_FROZEN);
		}
		User user = new User();
		BeanUtils.copyProperties(sysUser, user);
		user.setUsername(sysUser.getUserName());
		user.setFullName(sysUser.getUserFullName());
		user.setUserType(UserType.SYS_USER.getType());
		return user;
	}
}
