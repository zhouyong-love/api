package com.cloudok.log.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudok.core.service.AbstractService;
import com.cloudok.log.mapper.SysLogMapper;
import com.cloudok.log.po.SysLogPO;
import com.cloudok.log.service.SysLogService;
import com.cloudok.log.vo.SysLogVO;

@Service
public class SysLogServiceImpl extends AbstractService<SysLogVO, SysLogPO> implements SysLogService{

	@Autowired
	public SysLogServiceImpl(SysLogMapper repository) {
		super(repository);
	}
}
