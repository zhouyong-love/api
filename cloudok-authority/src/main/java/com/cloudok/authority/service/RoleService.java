package com.cloudok.authority.service;

import com.cloudok.authority.po.RolePO;
import com.cloudok.authority.service.grant.GrantObjectDynaSQL;
import com.cloudok.authority.vo.RoleVO;
import com.cloudok.core.service.IService;

import java.util.List;

public interface RoleService extends IService<RoleVO,RolePO>{

	List<RoleVO> findRolesByObject(String objectType,Long objectId);
	
	List<RoleVO> findUserRoles(GrantObjectDynaSQL dynaSQL);
}
