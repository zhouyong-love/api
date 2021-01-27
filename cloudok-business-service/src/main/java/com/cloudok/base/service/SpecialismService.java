package com.cloudok.base.service;

import com.cloudok.core.service.IService;

import java.util.List;

import com.cloudok.base.po.SpecialismPO;
import com.cloudok.base.vo.SpecialismVO;

public interface SpecialismService extends IService<SpecialismVO,SpecialismPO>{

	List<SpecialismVO> listBySchool(Long schoolId);
}
