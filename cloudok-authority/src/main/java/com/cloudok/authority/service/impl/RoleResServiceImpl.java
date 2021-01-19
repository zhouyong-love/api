package com.cloudok.authority.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloudok.authority.mapper.RoleResMapper;
import com.cloudok.authority.mapping.ResourceMapping;
import com.cloudok.authority.mapping.RoleResMapping;
import com.cloudok.authority.po.RoleResPO;
import com.cloudok.authority.service.ResourceService;
import com.cloudok.authority.service.RoleResService;
import com.cloudok.authority.service.RoleService;
import com.cloudok.authority.vo.ResourceVO;
import com.cloudok.authority.vo.RoleResVO;
import com.cloudok.authority.vo.RoleVO;
import com.cloudok.cache.CacheNameSpace;
import com.cloudok.core.exception.SystemException;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.service.AbstractService;

@Service
public class RoleResServiceImpl extends AbstractService<RoleResVO, RoleResPO> implements RoleResService {

	public static final CacheNameSpace JURISDICTIONCACHE = new CacheNameSpace("jurisdiction", "system");

	@Autowired
	private RoleResMapper mapper;

	@Autowired
	public RoleResServiceImpl(RoleResMapper repository) {
		super(repository);
	}

	@Autowired
	private RoleService roleService;

	@Autowired
	private ResourceService resourceService;

	@Override
	public RoleResVO create(RoleResVO d) {
		if (super.count(QueryBuilder.create(RoleResMapping.class).and(RoleResMapping.RESID, d.getResId())
				.and(RoleResMapping.ROLEID, d.getRoleId()).end()) > 0) {
			throw new SystemException("Data error!");
		}
		RoleVO role = roleService.get(d.getRoleId());
		if (role == null) {
			throw new SystemException("The role does not exist.");
		}
		if (resourceService
				.count(QueryBuilder.create(ResourceMapping.class).and(ResourceMapping.ID, d.getResId()).end()) <= 0) {
			throw new SystemException("The resource does not exist.");
		}
		RoleResVO roleRes = super.create(d);
		return roleRes;
	}


	@Transactional
	@Override
	public List<RoleResVO> create(List<RoleResVO> ds) {
		for (RoleResVO roleResVO : ds) {
			create(roleResVO);
		}
		return ds;
	}

	@Override
	public void deleteByRole(List<Long> roleIds) {
		mapper.deleteByRole(roleIds);
	}


	@Override
	public List<ResourceVO> getResourceByRole(List<Long> roles) {
		return resourceService.queryRoleRes(roles);
	}
}
