package com.cloudok.base.service;

import com.cloudok.core.service.IService;
import com.cloudok.base.po.ResearchDomainPO;
import com.cloudok.base.vo.ResearchDomainVO;

public interface ResearchDomainService extends IService<ResearchDomainVO, ResearchDomainPO> {

	public ResearchDomainVO createOrGet(String name);
	
}
