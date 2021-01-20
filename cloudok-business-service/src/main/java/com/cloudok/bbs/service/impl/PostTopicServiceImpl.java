package com.cloudok.bbs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudok.core.service.AbstractService;
import com.cloudok.bbs.mapper.PostTopicMapper;
import com.cloudok.bbs.po.PostTopicPO;
import com.cloudok.bbs.service.PostTopicService;
import com.cloudok.bbs.vo.PostTopicVO;

@Service
public class PostTopicServiceImpl extends AbstractService<PostTopicVO, PostTopicPO> implements PostTopicService{

	@Autowired
	public PostTopicServiceImpl(PostTopicMapper repository) {
		super(repository);
	}
}
