package com.cloudok.base.service;

import com.cloudok.base.po.CompanyPO;
import com.cloudok.base.vo.CompanyVO;
import com.cloudok.core.service.IService;

public interface CompanyService extends IService<CompanyVO, CompanyPO> {
	public CompanyVO createOrGet(String name);
}
