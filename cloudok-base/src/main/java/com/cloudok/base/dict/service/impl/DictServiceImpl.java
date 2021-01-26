package com.cloudok.base.dict.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.cloudok.base.dict.enums.EnumHandler;
import com.cloudok.base.dict.enums.vo.EnumVO;
import com.cloudok.base.dict.mapper.DictMapper;
import com.cloudok.base.dict.mapping.DictDataMapping;
import com.cloudok.base.dict.mapping.DictMapping;
import com.cloudok.base.dict.po.DictPO;
import com.cloudok.base.dict.service.DictDataService;
import com.cloudok.base.dict.service.DictService;
import com.cloudok.base.dict.vo.DictDataVO;
import com.cloudok.base.dict.vo.DictVO;
import com.cloudok.cache.Cache;
import com.cloudok.cache.CacheNameSpace;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.query.QueryOperator;
import com.cloudok.core.service.AbstractService;

@Service
public class DictServiceImpl extends AbstractService<DictVO, DictPO> implements DictService{
	
	public static final CacheNameSpace DICTCACHE=new CacheNameSpace("base", "dict");

	@Autowired
	public DictServiceImpl(DictMapper repository) {
		super(repository);
	}
	
	@Autowired
	private DictDataService dictDataService;

	@Autowired
	private Cache cache;
	
	@Transactional
	@Override
	public Integer remove(Long pk) {
		DictVO dictVO = super.get(pk);
		List<DictDataVO> dictDatas= dictDataService.list(QueryBuilder.create(DictDataMapping.class).and(DictDataMapping.DICTCODE, dictVO.getDictCode()).end());
		if(dictDatas!=null) {
			dictDataService.remove(dictDatas.stream().map(item->item.getId()).collect(Collectors.toList()));
		}
		Integer r=super.remove(Collections.singletonList(pk));
		removeCache(dictVO.getDictCode());
		return r;
	}



	@Override
	public List<DictVO> findAll(String dictCode, String dictName, String remark) {
		List<DictVO> list=new ArrayList<>();
		EnumHandler.getEnums().forEach(item->{
			if((StringUtils.isEmpty(dictCode)||item.getType().indexOf(dictCode)>=0)&&(StringUtils.isEmpty(dictName)||item.getLabel().indexOf(dictName)>=0)&&(StringUtils.isEmpty(remark)||item.getDescrib().indexOf(remark)>=0)) {
				DictVO vo=new DictVO();
				vo.setDictCode(item.getType());
				vo.setDictName(item.getLabel());
				vo.setRemark(item.getDescrib());
				vo.setBuiltInSystem(true);
				list.add(vo);
			}
		});
		list.addAll(super.list(QueryBuilder.create(DictMapping.class).and(DictMapping.DICTCODE, QueryOperator.LIKE,dictCode).and(DictMapping.DICTNAME, QueryOperator.LIKE,dictName).and(DictMapping.REMARK, QueryOperator.LIKE,remark).end()));
		return list;
	}
	
	public void reflashCache(String code) {
		List<DictDataVO> dicList = dictDataService.list(QueryBuilder.create(DictDataMapping.class).and(DictDataMapping.DICTCODE, code).end().sort(DictDataMapping.SN).desc());
		cache.put(DICTCACHE, code,dicList);
	}
	
	public void removeCache(String dictCode) {
		cache.evict(DICTCACHE, dictCode);
	}

	@Override
	public List<DictDataVO> findAllFromCache(String dictCode) {
		List<DictDataVO> list=new ArrayList<DictDataVO>();
		EnumVO e = EnumHandler.getEnum(dictCode);
		if(e!=null && !CollectionUtils.isEmpty(e.getValues())) {
			e.getValues().forEach(item->{
				DictDataVO dataVO=new DictDataVO();
				dataVO.setDictCode(dictCode);
				dataVO.setDictShowName(item.getDescribe());
				dataVO.setDictValue(item.getValue());
				dataVO.setRemark(item.getDescribe());
				dataVO.setSn(item.getSn());
				dataVO.setBuiltInSystem(true);
				list.add(dataVO);
			});
		}
		@SuppressWarnings("unchecked")
		List<DictDataVO> cacheData= cache.get(DICTCACHE, dictCode, ArrayList.class);
		if(cacheData!=null) {
			list.addAll(cacheData);
		}
		list.sort(new Comparator<DictDataVO>() {
			@Override
			public int compare(DictDataVO o1, DictDataVO o2) {
				if(o1.getSn()==null) {
					o1.setSn(0L);
				}
				if(o2.getSn()==null) {
					o2.setSn(0L);
				}
				return o1.getSn()>o2.getSn()?1:-1;
			}
		});
		return list;
	}



	@Override
	public void reflashCache() {
		this.list(QueryBuilder.create(DictMapping.class)).forEach(item->{
			this.reflashCache(item.getDictCode());
		});
	}
}
