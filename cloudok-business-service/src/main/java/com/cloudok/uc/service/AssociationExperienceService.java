package com.cloudok.uc.service;

import javax.validation.Valid;

import com.cloudok.core.service.IService;
import com.cloudok.uc.po.AssociationExperiencePO;
import com.cloudok.uc.vo.AssociationExperienceVO;
import com.cloudok.uc.vo.SwitchSNRequest;

public interface AssociationExperienceService extends IService<AssociationExperienceVO,AssociationExperiencePO>{

	Object switchSN(@Valid SwitchSNRequest switchSNRequest);

}
