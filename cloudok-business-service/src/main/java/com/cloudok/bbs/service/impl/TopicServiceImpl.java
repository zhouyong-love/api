package com.cloudok.bbs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudok.core.service.AbstractService;
import com.cloudok.bbs.mapper.TopicMapper;
import com.cloudok.bbs.po.TopicPO;
import com.cloudok.bbs.service.TopicService;
import com.cloudok.bbs.vo.TopicVO;

@Service
public class TopicServiceImpl extends AbstractService<TopicVO, TopicPO> implements TopicService{

	@Autowired
	public TopicServiceImpl(TopicMapper repository) {
		super(repository);
	}
}
