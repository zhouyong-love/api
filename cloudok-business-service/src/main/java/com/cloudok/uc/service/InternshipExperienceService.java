package com.cloudok.uc.service;

import java.util.List;

import javax.validation.Valid;

import com.cloudok.core.service.IService;
import com.cloudok.uc.po.InternshipExperiencePO;
import com.cloudok.uc.vo.InternshipExperienceVO;
import com.cloudok.uc.vo.SwitchSNRequest;

public interface InternshipExperienceService extends IService<InternshipExperienceVO,InternshipExperiencePO>{

	List<InternshipExperienceVO> getByMember(Long currentUserId);
	
	InternshipExperienceVO getByMember(Long currentUserId,Long id);

	Object switchSN(@Valid SwitchSNRequest switchSNRequest);


}
