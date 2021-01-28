package com.cloudok.uc.service;

import java.util.List;

import com.cloudok.core.service.IService;
import com.cloudok.uc.po.InternshipExperiencePO;
import com.cloudok.uc.vo.InternshipExperienceVO;

public interface InternshipExperienceService extends IService<InternshipExperienceVO,InternshipExperiencePO>{

	List<InternshipExperienceVO> getByMember(Long currentUserId);
	
	InternshipExperienceVO getByMember(Long currentUserId,Long id);


}
