package com.cloudok.uc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudok.core.service.AbstractService;
import com.cloudok.uc.mapper.MemberTagsMapper;
import com.cloudok.uc.po.MemberTagsPO;
import com.cloudok.uc.service.MemberTagsService;
import com.cloudok.uc.vo.MemberTagsVO;

@Service
public class MemberTagsServiceImpl extends AbstractService<MemberTagsVO, MemberTagsPO> implements MemberTagsService{

	@Autowired
	public MemberTagsServiceImpl(MemberTagsMapper repository) {
		super(repository);
	}
}
