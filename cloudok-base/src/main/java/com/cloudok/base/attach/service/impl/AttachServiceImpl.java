package com.cloudok.base.attach.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudok.base.attach.mapper.AttachMapper;
import com.cloudok.base.attach.po.AttachPO;
import com.cloudok.base.attach.service.AttachService;
import com.cloudok.base.attach.vo.AttachVO;
import com.cloudok.core.service.AbstractService;

@Service
public class AttachServiceImpl extends AbstractService<AttachVO, AttachPO> implements AttachService{

	@Autowired
	public AttachServiceImpl(AttachMapper repository) {
		super(repository);
	}
}
