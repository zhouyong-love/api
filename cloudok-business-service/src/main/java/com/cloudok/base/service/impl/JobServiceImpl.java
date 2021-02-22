package com.cloudok.base.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cloudok.base.mapper.JobMapper;
import com.cloudok.base.mapping.JobMapping;
import com.cloudok.base.po.JobPO;
import com.cloudok.base.service.JobService;
import com.cloudok.base.vo.JobVO;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.service.AbstractService;

@Service
public class JobServiceImpl extends AbstractService<JobVO, JobPO> implements JobService {

	@Autowired
	public JobServiceImpl(JobMapper repository) {
		super(repository);
	}

	@Override
	public JobVO createOrGet(String name) {
		List<JobVO> list = this
				.list(QueryBuilder.create(JobMapping.class).and(JobMapping.NAME, name.trim()).end());
		if (!CollectionUtils.isEmpty(list)) {
			return list.get(0);
		}
		JobVO vo = new JobVO();
		vo.setName(name.trim());
		JobVO v = this.get(QueryBuilder.create(JobMapping.class).sort(JobMapping.SN).desc());
		if(v != null) {
			vo.setSn(v.getSn()+1);
		}else {
			vo.setSn(99999);
		}
		this.create(vo);
		return vo;
	}
}
