package com.cloudok.base.dict.service;

import java.util.List;

import com.cloudok.base.dict.po.DictPO;
import com.cloudok.base.dict.vo.DictDataVO;
import com.cloudok.base.dict.vo.DictVO;
import com.cloudok.core.service.IService;

public interface DictService extends IService<DictVO, DictPO> {

	List<DictVO> findAll(String dictCode, String dictName, String remark);

	void reflashCache(String code);

	void removeCache(String dictCode);
	
	List<DictDataVO> findAllFromCache(String dictCode);

}
