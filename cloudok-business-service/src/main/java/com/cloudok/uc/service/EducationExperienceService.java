package com.cloudok.uc.service;

import java.util.List;

import javax.validation.Valid;

import com.cloudok.core.service.IService;
import com.cloudok.uc.po.EducationExperiencePO;
import com.cloudok.uc.vo.EducationExperienceVO;
import com.cloudok.uc.vo.SwitchSNRequest;

public interface EducationExperienceService extends IService<EducationExperienceVO,EducationExperiencePO>{

	List<EducationExperienceVO> getByMember(Long currentUserId);

	EducationExperienceVO getByMember(Long currentUserId,Long educationId);

	Object switchSN(@Valid SwitchSNRequest switchSNRequest);

}
