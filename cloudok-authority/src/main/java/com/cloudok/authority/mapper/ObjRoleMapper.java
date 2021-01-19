package com.cloudok.authority.mapper;

import com.cloudok.authority.po.ObjRolePO;
import com.cloudok.authority.service.grant.GrantObjectDynaSQL;
import com.cloudok.core.mapper.IMapper;

import java.util.List;
import java.util.Map;

public interface ObjRoleMapper extends IMapper<ObjRolePO>{

	Long queryObjectCount(GrantObjectDynaSQL dynaSQL);
	
	List<Map<String, ?>> queryObjectPage(GrantObjectDynaSQL sql);
	
	void deleteByRole(List<Long> roleIds);
}
