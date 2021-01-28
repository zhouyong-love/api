package com.cloudok.uc.service;

import java.util.List;

import com.cloudok.core.service.IService;
import com.cloudok.uc.po.ProjectExperiencePO;
import com.cloudok.uc.vo.ProjectExperienceVO;

public interface ProjectExperienceService extends IService<ProjectExperienceVO,ProjectExperiencePO>{

	List<ProjectExperienceVO> getByMember(Long currentUserId);
	
	ProjectExperienceVO getByMember(Long currentUserId,Long id);
}
