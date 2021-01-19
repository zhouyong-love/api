package com.cloudok.authority.service.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudok.authority.mapper.UserMapper;
import com.cloudok.authority.mapping.UserMapping;
import com.cloudok.authority.po.UserPO;
import com.cloudok.authority.service.UserEventHandler;
import com.cloudok.authority.service.UserService;
import com.cloudok.authority.vo.UserVO;
import com.cloudok.core.context.SpringApplicationContext;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.query.QueryOperator;
import com.cloudok.core.service.AbstractService;

@Service
public class UserServiceImpl extends AbstractService<UserVO, UserPO> implements UserService{

	@Autowired
	public UserServiceImpl(UserMapper repository) {
		super(repository);
	}

	@Override
	public UserVO create(UserVO d) {
		UserVO user = super.create(d);
		Collection<UserEventHandler> events = SpringApplicationContext.getBeanOfType(UserEventHandler.class);
		if(events!=null) {
			events.forEach(item->{
				item.addUser(user);
			});
		}
		return user;
	}

	@Override
	public List<UserVO> create(List<UserVO> ds) {
		for (UserVO userVO : ds) {
			create(userVO);
		}
		return ds;
	}

	@Override
	public UserVO merge(UserVO d) {
		Collection<UserEventHandler> events = SpringApplicationContext.getBeanOfType(UserEventHandler.class);
		if(events!=null) {
			events.forEach(item->{
				item.updateUser(d);
			});
		}
		return super.merge(d);
	}

	@Override
	public UserVO update(UserVO d) {
		Collection<UserEventHandler> events = SpringApplicationContext.getBeanOfType(UserEventHandler.class);
		if(events!=null) {
			events.forEach(item->{
				item.addUser(d);
			});
		}
		return super.update(d);
	}

	@Override
	public Integer remove(Long pk) {
		return remove(Collections.singletonList(pk));
	}

	@Override
	public Integer remove(List<Long> pkList) {
		Collection<UserEventHandler> events = SpringApplicationContext.getBeanOfType(UserEventHandler.class);
		for (Long userid : pkList) {
			if(events!=null) {
				events.forEach(item->{
					item.deleteUser(userid);
				});
			}
		}
		return super.remove(pkList);
	}

	@Override
	public boolean exists(String username) {
		return list(QueryBuilder.create(UserMapping.class).and(UserMapping.USERNAME, QueryOperator.EQ,username).end()).stream().findAny().isPresent();
	}
}
