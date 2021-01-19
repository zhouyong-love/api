package com.cloudok.base.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudok.base.mapper.AttachMapper;
import com.cloudok.base.po.AttachPO;
import com.cloudok.base.service.AttachService;
import com.cloudok.base.vo.AttachVO;
import com.cloudok.core.service.AbstractService;

@Service
public class AttachServiceImpl extends AbstractService<AttachVO, AttachPO> implements AttachService{

	@Autowired
	public AttachServiceImpl(AttachMapper repository) {
		super(repository);
	}
}
