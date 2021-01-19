package com.cloudok.base.service;

import com.cloudok.base.po.DictPO;
import com.cloudok.base.vo.DictDataVO;
import com.cloudok.base.vo.DictVO;
import com.cloudok.core.service.IService;

import java.util.List;

public interface DictService extends IService<DictVO, DictPO> {

	List<DictVO> findAll(String dictCode, String dictName, String remark);

	void reflashCache(String code);

	void removeCache(String dictCode);
	
	List<DictDataVO> findAllFromCache(String dictCode);

}
