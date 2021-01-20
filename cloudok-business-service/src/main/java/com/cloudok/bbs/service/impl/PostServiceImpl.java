package com.cloudok.bbs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudok.core.service.AbstractService;
import com.cloudok.bbs.mapper.PostMapper;
import com.cloudok.bbs.po.PostPO;
import com.cloudok.bbs.service.PostService;
import com.cloudok.bbs.vo.PostVO;

@Service
public class PostServiceImpl extends AbstractService<PostVO, PostPO> implements PostService{

	@Autowired
	public PostServiceImpl(PostMapper repository) {
		super(repository);
	}
}
