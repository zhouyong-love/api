package com.cloudok.base.dict.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudok.base.dict.enums.EnumHandler;
import com.cloudok.base.dict.mapper.DictDataMapper;
import com.cloudok.base.dict.mapping.DictDataMapping;
import com.cloudok.base.dict.po.DictDataPO;
import com.cloudok.base.dict.service.DictDataService;
import com.cloudok.base.dict.vo.DictDataVO;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.service.AbstractService;

@Service
public class DictDataServiceImpl extends AbstractService<DictDataVO, DictDataPO> implements DictDataService{

	@Autowired
	public DictDataServiceImpl(DictDataMapper repository) {
		super(repository);
	}
	
	@Autowired
	private DictServiceImpl dictService;

	@Override
	public DictDataVO create(DictDataVO d) {
		DictDataVO result = super.create(d);
		dictService.reflashCache(d.getDictCode());
		return result;
	}

	@Override
	public DictDataVO update(DictDataVO d) {
		super.update(d);
		dictService.reflashCache(d.getDictCode());
		return d;
	}

	@Override
	public Integer remove(Long pk) {
		Integer r=super.remove(pk);
		dictService.reflashCache(super.get(pk).getDictCode());
		return r;
	}

	@Override
	public List<DictDataVO> findAll(String dictCode) {
		List<DictDataVO> list=new ArrayList<DictDataVO>();
		EnumHandler.getEnum(dictCode).getValues().forEach(item->{
			DictDataVO dataVO=new DictDataVO();
			dataVO.setDictCode(dictCode);
			dataVO.setDictShowName(item.getDescribe());
			dataVO.setDictValue(item.getValue());
			dataVO.setRemark(item.getDescribe());
			dataVO.setSn(item.getSn());
			dataVO.setBuiltInSystem(true);
			list.add(dataVO);
		});
		list.addAll(super.list(QueryBuilder.create(DictDataMapping.class).and(DictDataMapping.DICTCODE, dictCode).end()));
		list.sort(new Comparator<DictDataVO>() {
			@Override
			public int compare(DictDataVO o1, DictDataVO o2) {
				return o1.getSn()>o2.getSn()?1:-1;
			}
		});
		return list;
	}
}
