package com.cloudok.base.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudok.base.mapper.SchoolMapper;
import com.cloudok.base.po.SchoolPO;
import com.cloudok.base.service.SchoolService;
import com.cloudok.base.vo.SchoolVO;
import com.cloudok.core.service.AbstractService;

@Service
public class SchoolServiceImpl extends AbstractService<SchoolVO, SchoolPO> implements SchoolService{

	@Autowired
	public SchoolServiceImpl(SchoolMapper repository) {
		super(repository);
	}

	
}
