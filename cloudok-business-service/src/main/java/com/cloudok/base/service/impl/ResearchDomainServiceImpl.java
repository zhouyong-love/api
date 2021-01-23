package com.cloudok.base.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cloudok.base.mapper.ResearchDomainMapper;
import com.cloudok.base.mapping.ResearchDomainMapping;
import com.cloudok.base.po.ResearchDomainPO;
import com.cloudok.base.service.ResearchDomainService;
import com.cloudok.base.vo.ResearchDomainVO;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.service.AbstractService;

@Service
public class ResearchDomainServiceImpl extends AbstractService<ResearchDomainVO, ResearchDomainPO> implements ResearchDomainService{

	@Autowired
	public ResearchDomainServiceImpl(ResearchDomainMapper repository) {
		super(repository);
	}

	@Override
	public ResearchDomainVO createOrGet(String name) {
		List<ResearchDomainVO> list =  this.list(QueryBuilder.create(ResearchDomainMapping.class)
				.and(ResearchDomainMapping.NAME, name.trim()).end());
		if(!CollectionUtils.isEmpty(list)) {
			return list.get(0);
		}
		ResearchDomainVO vo  = new ResearchDomainVO();
		vo.setName(name.trim());
		this.create(vo);
		return vo;
	}
}
