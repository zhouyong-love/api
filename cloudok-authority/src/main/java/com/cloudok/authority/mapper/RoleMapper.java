package com.cloudok.authority.mapper;

import com.cloudok.authority.po.ObjRolePO;
import com.cloudok.authority.po.RolePO;
import com.cloudok.authority.service.grant.GrantObjectDynaSQL;
import com.cloudok.core.mapper.IMapper;

import java.util.List;

public interface RoleMapper extends IMapper<RolePO>{

	List<RolePO> findRolesByObject(ObjRolePO objRolePO);
	
	List<RolePO> findUserRoles(GrantObjectDynaSQL query);
}
