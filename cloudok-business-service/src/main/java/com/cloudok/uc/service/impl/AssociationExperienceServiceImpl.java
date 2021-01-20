package com.cloudok.uc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudok.core.service.AbstractService;
import com.cloudok.uc.mapper.AssociationExperienceMapper;
import com.cloudok.uc.po.AssociationExperiencePO;
import com.cloudok.uc.service.AssociationExperienceService;
import com.cloudok.uc.vo.AssociationExperienceVO;

@Service
public class AssociationExperienceServiceImpl extends AbstractService<AssociationExperienceVO, AssociationExperiencePO> implements AssociationExperienceService{

	@Autowired
	public AssociationExperienceServiceImpl(AssociationExperienceMapper repository) {
		super(repository);
	}
}
