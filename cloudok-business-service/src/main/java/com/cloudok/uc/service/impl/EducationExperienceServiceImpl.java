package com.cloudok.uc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudok.core.service.AbstractService;
import com.cloudok.uc.mapper.EducationExperienceMapper;
import com.cloudok.uc.po.EducationExperiencePO;
import com.cloudok.uc.service.EducationExperienceService;
import com.cloudok.uc.vo.EducationExperienceVO;

@Service
public class EducationExperienceServiceImpl extends AbstractService<EducationExperienceVO, EducationExperiencePO> implements EducationExperienceService{

	@Autowired
	public EducationExperienceServiceImpl(EducationExperienceMapper repository) {
		super(repository);
	}
}
