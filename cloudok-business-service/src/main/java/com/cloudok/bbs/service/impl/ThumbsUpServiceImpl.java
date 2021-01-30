package com.cloudok.bbs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudok.core.service.AbstractService;
import com.cloudok.bbs.mapper.ThumbsUpMapper;
import com.cloudok.bbs.po.ThumbsUpPO;
import com.cloudok.bbs.service.ThumbsUpService;
import com.cloudok.bbs.vo.ThumbsUpVO;

@Service
public class ThumbsUpServiceImpl extends AbstractService<ThumbsUpVO, ThumbsUpPO> implements ThumbsUpService{

	@Autowired
	public ThumbsUpServiceImpl(ThumbsUpMapper repository) {
		super(repository);
	}
}
