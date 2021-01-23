package com.cloudok.base.service;

import com.cloudok.base.po.JobPO;
import com.cloudok.base.vo.JobVO;
import com.cloudok.core.service.IService;

public interface JobService extends IService<JobVO, JobPO> {
	public JobVO createOrGet(String name);
}
