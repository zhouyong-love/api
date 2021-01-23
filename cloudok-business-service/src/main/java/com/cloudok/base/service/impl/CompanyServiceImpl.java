package com.cloudok.base.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cloudok.base.mapper.CompanyMapper;
import com.cloudok.base.mapping.CompanyMapping;
import com.cloudok.base.po.CompanyPO;
import com.cloudok.base.service.CompanyService;
import com.cloudok.base.vo.CompanyVO;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.service.AbstractService;

@Service
public class CompanyServiceImpl extends AbstractService<CompanyVO, CompanyPO> implements CompanyService {

	@Autowired
	public CompanyServiceImpl(CompanyMapper repository) {
		super(repository);
	}

	@Override
	public CompanyVO createOrGet(String name) {
		List<CompanyVO> list = this.list(
				QueryBuilder.create(CompanyMapping.class).and(CompanyMapping.NAME, name.trim()).end());
		if (!CollectionUtils.isEmpty(list)) {
			return list.get(0);
		}
		CompanyVO vo = new CompanyVO();
		vo.setName(name.trim());
		this.create(vo);
		return vo;
	}
}
