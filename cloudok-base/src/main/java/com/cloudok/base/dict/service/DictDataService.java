package com.cloudok.base.dict.service;

import java.util.List;

import com.cloudok.base.dict.po.DictDataPO;
import com.cloudok.base.dict.vo.DictDataVO;
import com.cloudok.core.service.IService;

public interface DictDataService extends IService<DictDataVO,DictDataPO>{

	List<DictDataVO> findAll(String dictCode);
	
}
