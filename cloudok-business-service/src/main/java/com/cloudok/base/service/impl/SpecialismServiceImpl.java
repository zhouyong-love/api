package com.cloudok.base.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudok.core.service.AbstractService;
import com.cloudok.base.mapper.SpecialismMapper;
import com.cloudok.base.po.SpecialismPO;
import com.cloudok.base.service.SpecialismService;
import com.cloudok.base.vo.SpecialismVO;

@Service
public class SpecialismServiceImpl extends AbstractService<SpecialismVO, SpecialismPO> implements SpecialismService{

	@Autowired
	public SpecialismServiceImpl(SpecialismMapper repository) {
		super(repository);
	}
}
