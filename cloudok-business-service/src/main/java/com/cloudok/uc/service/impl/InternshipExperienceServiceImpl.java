package com.cloudok.uc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudok.core.service.AbstractService;
import com.cloudok.uc.mapper.InternshipExperienceMapper;
import com.cloudok.uc.po.InternshipExperiencePO;
import com.cloudok.uc.service.InternshipExperienceService;
import com.cloudok.uc.vo.InternshipExperienceVO;

@Service
public class InternshipExperienceServiceImpl extends AbstractService<InternshipExperienceVO, InternshipExperiencePO> implements InternshipExperienceService{

	@Autowired
	public InternshipExperienceServiceImpl(InternshipExperienceMapper repository) {
		super(repository);
	}
}
