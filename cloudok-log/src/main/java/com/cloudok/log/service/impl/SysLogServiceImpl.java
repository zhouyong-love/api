package com.cloudok.log.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cloudok.core.context.SpringApplicationContext;
import com.cloudok.core.service.AbstractService;
import com.cloudok.log.event.UserActionEvent;
import com.cloudok.log.mapper.SysLogMapper;
import com.cloudok.log.po.SysLogPO;
import com.cloudok.log.service.SysLogService;
import com.cloudok.log.vo.SysLogVO;

@Service
public class SysLogServiceImpl extends AbstractService<SysLogVO, SysLogPO> implements SysLogService {
	private static final int MAX_INTERFACE_NAME_LENGTH = 50;
	private static final int MAX_INPUT_LENGTH = 1024;
	private static final int MAX_OUT_LENGTH = 1024;

	@Autowired
	public SysLogServiceImpl(SysLogMapper repository) {
		super(repository);
	}

	@Override
	public SysLogVO create(SysLogVO d) {
		if (!StringUtils.isEmpty(d.getInterfaceName()) && d.getInterfaceName().length() > MAX_INTERFACE_NAME_LENGTH) {
			d.setInterfaceName(d.getInterfaceName().substring(0, MAX_INTERFACE_NAME_LENGTH));
		}
		if (!StringUtils.isEmpty(d.getInput()) && d.getInput().length() > MAX_INPUT_LENGTH) {
			d.setInput(d.getInput().substring(0, MAX_INPUT_LENGTH));
		}
		if (!StringUtils.isEmpty(d.getOutput()) && d.getOutput().length() > MAX_OUT_LENGTH) {
			d.setOutput(d.getOutput().substring(0, MAX_OUT_LENGTH));
		}
		SysLogVO log = super.create(d);
		SpringApplicationContext.publishEvent(new UserActionEvent(log));
		return log;
	}
}
