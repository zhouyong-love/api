package com.cloudok.base.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cloudok.base.event.TopicCreateEvent;
import com.cloudok.base.mapper.ResearchDomainMapper;
import com.cloudok.base.mapping.ResearchDomainMapping;
import com.cloudok.base.po.ResearchDomainPO;
import com.cloudok.base.service.ResearchDomainService;
import com.cloudok.base.vo.ResearchDomainVO;
import com.cloudok.base.vo.TopicInfo;
import com.cloudok.core.context.SpringApplicationContext;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.service.AbstractService;
import com.cloudok.enums.BBSTopicType;

@Service
public class ResearchDomainServiceImpl extends AbstractService<ResearchDomainVO, ResearchDomainPO> implements ResearchDomainService{

	@Autowired
	public ResearchDomainServiceImpl(ResearchDomainMapper repository) {
		super(repository);
	}

	@Override
	public ResearchDomainVO createOrGet(String name) {
		List<ResearchDomainVO> list =  this.list(QueryBuilder.create(ResearchDomainMapping.class)
				.and(ResearchDomainMapping.NAME, name.trim()).end());
		if(!CollectionUtils.isEmpty(list)) {
			return list.get(0);
		}
		ResearchDomainVO vo  = new ResearchDomainVO();
		vo.setName(name.trim());
		ResearchDomainVO v = this.get(QueryBuilder.create(ResearchDomainMapping.class).sort(ResearchDomainMapping.SN).desc());
		if(v != null) {
			vo.setSn(v.getSn()+1);
		}else {
			vo.setSn(99999);
		}
		this.create(vo);
		
		SpringApplicationContext.publishEvent(new TopicCreateEvent(TopicInfo.builder()
				.forceUpate(false)
				.topicId(vo.getId())
				.topicName(vo.getName())
				.topicType(Integer.parseInt(BBSTopicType.researchDomain.getValue()))
				.build())
		);
		return vo;
	}
}
