package com.cloudok.base.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudok.core.service.AbstractService;
import com.cloudok.base.mapper.AssociationMapper;
import com.cloudok.base.po.AssociationPO;
import com.cloudok.base.service.AssociationService;
import com.cloudok.base.vo.AssociationVO;

@Service
public class AssociationServiceImpl extends AbstractService<AssociationVO, AssociationPO> implements AssociationService{

	@Autowired
	public AssociationServiceImpl(AssociationMapper repository) {
		super(repository);
	}
}
