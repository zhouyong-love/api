package com.cloudok.base.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudok.core.service.AbstractService;
import com.cloudok.base.mapper.SchoolSpecialitiesMapper;
import com.cloudok.base.po.SchoolSpecialitiesPO;
import com.cloudok.base.service.SchoolSpecialitiesService;
import com.cloudok.base.vo.SchoolSpecialitiesVO;

@Service
public class SchoolSpecialitiesServiceImpl extends AbstractService<SchoolSpecialitiesVO, SchoolSpecialitiesPO> implements SchoolSpecialitiesService{

	@Autowired
	public SchoolSpecialitiesServiceImpl(SchoolSpecialitiesMapper repository) {
		super(repository);
	}
}
