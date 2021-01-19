package com.cloudok.authority.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloudok.authority.mapper.RoleMapper;
import com.cloudok.authority.po.ObjRolePO;
import com.cloudok.authority.po.RolePO;
import com.cloudok.authority.service.ObjRoleService;
import com.cloudok.authority.service.RoleResService;
import com.cloudok.authority.service.RoleService;
import com.cloudok.authority.service.grant.GrantObjectDynaSQL;
import com.cloudok.authority.vo.RoleVO;
import com.cloudok.core.service.AbstractService;

@Service
public class RoleServiceImpl extends AbstractService<RoleVO, RolePO> implements RoleService{

	private RoleMapper mapper;
	
	@Autowired
	private RoleResService roleResServer;
	
	@Autowired
	private ObjRoleService objectRoleServer;
	
	@Autowired
	public RoleServiceImpl(RoleMapper repository) {
		super(repository);
		this.mapper=repository;
	}

	@Override
	public List<RoleVO> findRolesByObject(String objectType, Long objectId) {
		ObjRolePO query=new ObjRolePO();
		query.setObjId(objectId);
		query.setObjType(objectType);
		return convert2VO(mapper.findRolesByObject(query));
	}

	@Override
	public List<RoleVO> findUserRoles(GrantObjectDynaSQL dynaSQL) {
		return convert2VO(mapper.findUserRoles(dynaSQL));
	}

	@Override
	public Integer remove(Long pk) {
		return remove(Collections.singletonList(pk));
	}

	@Transactional
	@Override
	public Integer remove(List<Long> pkList) {
		objectRoleServer.deleteByRole(pkList);
		roleResServer.deleteByRole(pkList);
		return super.remove(pkList);
	}
}
