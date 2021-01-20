package com.cloudok.base.service.impl;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudok.core.service.AbstractService;
import com.cloudok.base.mapper.TagMapper;
import com.cloudok.base.po.TagPO;
import com.cloudok.base.service.TagService;
import com.cloudok.base.vo.TagVO;

@Service
public class TagServiceImpl extends AbstractService<TagVO, TagPO> implements TagService{

	@Autowired
	public TagServiceImpl(TagMapper repository) {
		super(repository);
	}

	@Override
	public TagVO createByMember(@Valid TagVO vo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TagVO updateByMember(@Valid TagVO vo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int removeByMember(Long id) {
		// TODO Auto-generated method stub
		return 0;
	}
}
