package com.cloudok.base.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudok.core.service.AbstractService;
import com.cloudok.base.mapper.IndustryMapper;
import com.cloudok.base.po.IndustryPO;
import com.cloudok.base.service.IndustryService;
import com.cloudok.base.vo.IndustryVO;

@Service
public class IndustryServiceImpl extends AbstractService<IndustryVO, IndustryPO> implements IndustryService{

	@Autowired
	public IndustryServiceImpl(IndustryMapper repository) {
		super(repository);
	}
}
