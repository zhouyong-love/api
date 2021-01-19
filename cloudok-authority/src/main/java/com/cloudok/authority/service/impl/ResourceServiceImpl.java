package com.cloudok.authority.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudok.authority.mapper.ResourceMapper;
import com.cloudok.authority.mapping.ResourceMapping;
import com.cloudok.authority.po.ResourcePO;
import com.cloudok.authority.service.ResourceService;
import com.cloudok.authority.service.RoleResService;
import com.cloudok.authority.service.grant.RoleGrantAdapter;
import com.cloudok.authority.vo.ResourceVO;
import com.cloudok.authority.vo.RoleVO;
import com.cloudok.core.exception.SystemException;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.query.QueryOperator;
import com.cloudok.core.service.AbstractService;

@Service
public class ResourceServiceImpl extends AbstractService<ResourceVO, ResourcePO> implements ResourceService{

	private ResourceMapper mapper;
	
	@Autowired
	private RoleGrantAdapter roleGrantAdapter;
	
	@Autowired
	public ResourceServiceImpl(ResourceMapper repository) {
		super(repository);
		this.mapper=repository;
	}

	@Override
	public List<ResourceVO> getResourcesByUser(Long userid) {
		List<RoleVO> roles = roleGrantAdapter.getUserRoles(userid);
		return roleResService.getResourceByRole(roles.stream().map(item->item.getId()).collect(Collectors.toList()));
	}

	@Override
	public List<ResourceVO> getMenu(Long userid) {
		return getResourcesByUser(userid);
	}

	@Override
	public List<ResourceVO> queryRoleRes(List<Long> roleIds) {
		if(roleIds==null||roleIds.size()==0) {
			return new ArrayList<ResourceVO>();
		}
		return convert2VO(mapper.queryRoleRes(roleIds));
	}
	
	@Autowired
	private RoleResService roleResService;
	
	
	@Override
	public ResourceVO update(ResourceVO vo) {
		if (vo.getParentId() != null && vo.getParentId().longValue() != 0) {
			if (super.get(vo.getParentId()) == null) {
				throw new SystemException("Data error.");
			}
		}
		
		return super.update(vo);
	}
	
	@Override
	public Integer remove(Long pk) {
		return remove(Collections.singletonList(pk));
	}

	@Override
	public Integer remove(List<Long> pkList) {
		if (super.count(QueryBuilder.create(ResourceMapping.class).and(ResourceMapping.PARENTID,QueryOperator.IN, pkList).end()) > 0) {
			throw new SystemException("Child node exists, parameter not allowed.");
		}
		return super.remove(pkList);
	}
}
