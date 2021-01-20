package com.cloudok.uc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudok.core.service.AbstractService;
import com.cloudok.uc.mapper.ResearchExperienceMapper;
import com.cloudok.uc.po.ResearchExperiencePO;
import com.cloudok.uc.service.ResearchExperienceService;
import com.cloudok.uc.vo.ResearchExperienceVO;

@Service
public class ResearchExperienceServiceImpl extends AbstractService<ResearchExperienceVO, ResearchExperiencePO> implements ResearchExperienceService{

	@Autowired
	public ResearchExperienceServiceImpl(ResearchExperienceMapper repository) {
		super(repository);
	}
}
