package com.cloudok.base.service;

import com.cloudok.base.po.DictDataPO;
import com.cloudok.base.vo.DictDataVO;
import com.cloudok.core.service.IService;

import java.util.List;

public interface DictDataService extends IService<DictDataVO,DictDataPO>{

	List<DictDataVO> findAll(String dictCode);
}
