package com.cloudok.uc.service;

import java.util.List;

import javax.validation.Valid;

import com.cloudok.core.service.IService;
import com.cloudok.uc.po.ResearchExperiencePO;
import com.cloudok.uc.vo.ResearchExperienceVO;
import com.cloudok.uc.vo.SwitchSNRequest;

public interface ResearchExperienceService extends IService<ResearchExperienceVO,ResearchExperiencePO>{

	List<ResearchExperienceVO> getByMember(Long currentUserId);
	
	ResearchExperienceVO getByMember(Long currentUserId,Long id);

	Object switchSN(@Valid SwitchSNRequest switchSNRequest);


}
