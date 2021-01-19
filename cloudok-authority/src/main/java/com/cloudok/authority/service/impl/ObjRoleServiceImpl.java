package com.cloudok.authority.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloudok.authority.mapper.ObjRoleMapper;
import com.cloudok.authority.mapping.RoleMapping;
import com.cloudok.authority.po.ObjRolePO;
import com.cloudok.authority.service.ObjRoleService;
import com.cloudok.authority.service.RoleService;
import com.cloudok.authority.service.grant.GrantObjectDynaSQL;
import com.cloudok.authority.service.grant.RoleGrantAdapter;
import com.cloudok.authority.vo.ObjRoleVO;
import com.cloudok.core.exception.SystemException;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.service.AbstractService;

@Service
public class ObjRoleServiceImpl extends AbstractService<ObjRoleVO, ObjRolePO> implements ObjRoleService{

	private ObjRoleMapper mapper;
	
	@Autowired
	public ObjRoleServiceImpl(ObjRoleMapper repository) {
		super(repository);
		this.mapper=repository;
	}

	@Override
	public Long queryObjectCount(GrantObjectDynaSQL dynaSQL) {
		return mapper.queryObjectCount(dynaSQL);
	}

	@Override
	public List<Map<String, ?>> queryObjectPage(GrantObjectDynaSQL sql) {
		return mapper.queryObjectPage(sql);
	}

	@Override
	public void deleteByRole(List<Long> roleIds) {
		mapper.deleteByRole(roleIds);
	}

	@Autowired
	private RoleGrantAdapter grantAdapter;
	
	@Autowired
	private RoleService roleService;
	
	@Override
	public ObjRoleVO create(ObjRoleVO d) {
		if(!grantAdapter.getConfiguer().containsKey(d.getObjType())) {
			throw new SystemException("Data error.");
		}
		if(roleService.count(QueryBuilder.create(RoleMapping.class).and(RoleMapping.ID, d.getRoleId()).end() )<=0) {
			throw new SystemException("Data error.");
		}
		return super.create(d);
	}

	@Transactional
	@Override
	public List<ObjRoleVO> create(List<ObjRoleVO> ds) {
		for (ObjRoleVO objRoleVO : ds) {
			create(objRoleVO);
		}
		return ds;
	}
	
	
}
