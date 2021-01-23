package com.cloudok.uc.service;

import java.util.List;

import com.cloudok.core.service.IService;
import com.cloudok.uc.po.ResearchExperiencePO;
import com.cloudok.uc.vo.ResearchExperienceVO;

public interface ResearchExperienceService extends IService<ResearchExperienceVO,ResearchExperiencePO>{

	List<ResearchExperienceVO> getByMember(Long currentUserId);

}
