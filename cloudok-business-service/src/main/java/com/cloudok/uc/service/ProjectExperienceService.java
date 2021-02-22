package com.cloudok.uc.service;

import java.util.List;

import javax.validation.Valid;

import com.cloudok.core.service.IService;
import com.cloudok.uc.po.ProjectExperiencePO;
import com.cloudok.uc.vo.ProjectExperienceVO;
import com.cloudok.uc.vo.SwitchSNRequest;

public interface ProjectExperienceService extends IService<ProjectExperienceVO,ProjectExperiencePO>{

	List<ProjectExperienceVO> getByMember(Long currentUserId);
	
	ProjectExperienceVO getByMember(Long currentUserId,Long id);

	Object switchSN(@Valid SwitchSNRequest switchSNRequest);
}
