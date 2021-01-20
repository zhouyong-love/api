package com.cloudok.uc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudok.core.service.AbstractService;
import com.cloudok.uc.mapper.ProjectExperienceMapper;
import com.cloudok.uc.po.ProjectExperiencePO;
import com.cloudok.uc.service.ProjectExperienceService;
import com.cloudok.uc.vo.ProjectExperienceVO;

@Service
public class ProjectExperienceServiceImpl extends AbstractService<ProjectExperienceVO, ProjectExperiencePO> implements ProjectExperienceService{

	@Autowired
	public ProjectExperienceServiceImpl(ProjectExperienceMapper repository) {
		super(repository);
	}
}
