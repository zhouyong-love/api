package com.cloudok.base.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudok.core.service.AbstractService;
import com.cloudok.base.mapper.ResearchDomainMapper;
import com.cloudok.base.po.ResearchDomainPO;
import com.cloudok.base.service.ResearchDomainService;
import com.cloudok.base.vo.ResearchDomainVO;

@Service
public class ResearchDomainServiceImpl extends AbstractService<ResearchDomainVO, ResearchDomainPO> implements ResearchDomainService{

	@Autowired
	public ResearchDomainServiceImpl(ResearchDomainMapper repository) {
		super(repository);
	}
}
