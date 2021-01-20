package com.cloudok.bbs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudok.core.service.AbstractService;
import com.cloudok.bbs.mapper.CommentMapper;
import com.cloudok.bbs.po.CommentPO;
import com.cloudok.bbs.service.CommentService;
import com.cloudok.bbs.vo.CommentVO;

@Service
public class CommentServiceImpl extends AbstractService<CommentVO, CommentPO> implements CommentService{

	@Autowired
	public CommentServiceImpl(CommentMapper repository) {
		super(repository);
	}
}
