package com.cloudok.base.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudok.core.service.AbstractService;
import com.cloudok.base.mapper.SpecialismMapper;
import com.cloudok.base.po.SpecialismPO;
import com.cloudok.base.service.SpecialismService;
import com.cloudok.base.vo.SpecialismVO;

@Service
public class SpecialismServiceImpl extends AbstractService<SpecialismVO, SpecialismPO> implements SpecialismService{

	private SpecialismMapper repository;
	
	@Autowired
	public SpecialismServiceImpl(SpecialismMapper repository) {
		super(repository);
		this.repository=repository;
	}

	@Override
	public List<SpecialismVO> listBySchool(Long schoolId) {
		return convert2VO(repository.listBySchool(schoolId));
	}
}
