package com.cloudok.base.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudok.core.service.AbstractService;
import com.cloudok.base.mapper.TopicMapper;
import com.cloudok.base.po.TopicPO;
import com.cloudok.base.service.TopicService;
import com.cloudok.base.vo.TopicVO;

@Service
public class TopicServiceImpl extends AbstractService<TopicVO, TopicPO> implements TopicService{

	@Autowired
	public TopicServiceImpl(TopicMapper repository) {
		super(repository);
	}
}
