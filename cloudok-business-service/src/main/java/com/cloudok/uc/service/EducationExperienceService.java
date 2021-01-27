package com.cloudok.uc.service;

import java.util.List;

import com.cloudok.core.service.IService;
import com.cloudok.uc.po.EducationExperiencePO;
import com.cloudok.uc.vo.EducationExperienceVO;

public interface EducationExperienceService extends IService<EducationExperienceVO,EducationExperiencePO>{

	List<EducationExperienceVO> getByMember(Long currentUserId);

	EducationExperienceVO getByMember(Long currentUserId,Long educationId);

}
