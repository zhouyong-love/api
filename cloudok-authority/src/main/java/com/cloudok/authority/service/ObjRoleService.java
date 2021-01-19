package com.cloudok.authority.service;

import com.cloudok.authority.po.ObjRolePO;
import com.cloudok.authority.service.grant.GrantObjectDynaSQL;
import com.cloudok.authority.vo.ObjRoleVO;
import com.cloudok.core.service.IService;

import java.util.List;
import java.util.Map;

public interface ObjRoleService extends IService<ObjRoleVO,ObjRolePO>{

	Long queryObjectCount(GrantObjectDynaSQL dynaSQL);
	
	List<Map<String, ?>> queryObjectPage(GrantObjectDynaSQL sql);
	
	void deleteByRole(List<Long> roleIds);
}
