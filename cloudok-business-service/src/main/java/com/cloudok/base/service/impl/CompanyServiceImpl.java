package com.cloudok.base.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudok.core.service.AbstractService;
import com.cloudok.base.mapper.CompanyMapper;
import com.cloudok.base.po.CompanyPO;
import com.cloudok.base.service.CompanyService;
import com.cloudok.base.vo.CompanyVO;

@Service
public class CompanyServiceImpl extends AbstractService<CompanyVO, CompanyPO> implements CompanyService{

	@Autowired
	public CompanyServiceImpl(CompanyMapper repository) {
		super(repository);
	}
}
