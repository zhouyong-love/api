package com.cloudok.uc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudok.core.service.AbstractService;
import com.cloudok.uc.mapper.MemberTopicMapper;
import com.cloudok.uc.po.MemberTopicPO;
import com.cloudok.uc.service.MemberTopicService;
import com.cloudok.uc.vo.MemberTopicVO;

@Service
public class MemberTopicServiceImpl extends AbstractService<MemberTopicVO, MemberTopicPO> implements MemberTopicService{

	@Autowired
	public MemberTopicServiceImpl(MemberTopicMapper repository) {
		super(repository);
	}
}
