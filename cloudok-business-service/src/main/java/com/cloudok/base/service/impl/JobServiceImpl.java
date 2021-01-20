package com.cloudok.base.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudok.core.service.AbstractService;
import com.cloudok.base.mapper.JobMapper;
import com.cloudok.base.po.JobPO;
import com.cloudok.base.service.JobService;
import com.cloudok.base.vo.JobVO;

@Service
public class JobServiceImpl extends AbstractService<JobVO, JobPO> implements JobService{

	@Autowired
	public JobServiceImpl(JobMapper repository) {
		super(repository);
	}
}
