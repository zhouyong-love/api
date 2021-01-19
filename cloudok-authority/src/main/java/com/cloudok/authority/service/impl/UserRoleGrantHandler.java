package com.cloudok.authority.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.cloudok.authority.mapping.UserMapping;
import com.cloudok.authority.po.UserPO;
import com.cloudok.authority.service.grant.GrantObjectInfo;
import com.cloudok.authority.service.grant.GrantObjectMapping;
import com.cloudok.authority.service.grant.RoleGrantHandler;


@Component
public class UserRoleGrantHandler extends RoleGrantHandler<UserPO>{

	@Override
	public GrantObjectInfo objectTypeInfo() {
		return new GrantObjectInfo("sys_user","System user");
	}

	@Override
	public Class<UserPO> objectDataType() {
		return UserPO.class;
	}

	@Override
	public List<Long> getObjectsByUser(Long user) {
		List<Long> list=new ArrayList<Long>();
		list.add(user);
		return list;
	}

	@Override
	public GrantObjectMapping objectMapping() {
		return new GrantObjectMapping("sys_user",UserMapping.class);
	}
}
