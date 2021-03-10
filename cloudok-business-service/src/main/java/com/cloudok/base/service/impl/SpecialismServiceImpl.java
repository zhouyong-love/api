package com.cloudok.base.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudok.base.mapper.SpecialismMapper;
import com.cloudok.base.mapping.SpecialismMapping;
import com.cloudok.base.po.SpecialismPO;
import com.cloudok.base.service.SpecialismService;
import com.cloudok.base.vo.SpecialismVO;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.service.AbstractService;

@Service
public class SpecialismServiceImpl extends AbstractService<SpecialismVO, SpecialismPO> implements SpecialismService{

//	private SpecialismMapper repository;
	
	@Autowired
	public SpecialismServiceImpl(SpecialismMapper repository) {
		super(repository);
	}

	@Override
	public List<SpecialismVO> listBySchool(Long schoolId) {
		List<SpecialismVO> list =   this.list(QueryBuilder.create(SpecialismMapping.class).sort(SpecialismMapping.SN).asc());
		return list;
	}
}
