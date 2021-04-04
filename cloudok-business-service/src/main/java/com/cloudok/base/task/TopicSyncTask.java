package com.cloudok.base.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.cloudok.base.dict.service.DictService;
import com.cloudok.base.dict.vo.DictDataVO;
import com.cloudok.base.event.TopicCreateEvent;
import com.cloudok.base.mapper.ResearchDomainMapper;
import com.cloudok.base.mapper.SchoolMapper;
import com.cloudok.base.mapper.SpecialismMapper;
import com.cloudok.base.mapper.TagMapper;
import com.cloudok.base.mapping.ResearchDomainMapping;
import com.cloudok.base.mapping.SchoolMapping;
import com.cloudok.base.mapping.SpecialismMapping;
import com.cloudok.base.mapping.TagMapping;
import com.cloudok.base.po.ResearchDomainPO;
import com.cloudok.base.po.SchoolPO;
import com.cloudok.base.po.SpecialismPO;
import com.cloudok.base.po.TagPO;
import com.cloudok.base.vo.TopicInfo;
import com.cloudok.core.context.SpringApplicationContext;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.enums.BBSTopicType;
import com.cloudok.enums.TagCategory;

@Component
public class TopicSyncTask  {

	@Autowired
	private TagMapper tagMapper;

	@Autowired
	private SchoolMapper schoolMapper;

	@Autowired
	private ResearchDomainMapper researchDomainMapper;

	@Autowired
	private SpecialismMapper specialismMapper;
	
	@Autowired
	private DictService dictService;


	
	public void afterPropertiesSet() throws Exception {
//		new Thread(() -> {
//			try {
//				Thread.sleep(TimeUnit.MINUTES.toMillis(1));
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			this.sysncTopics();
//
//		}).start();
		this.sysncTopics();
	}

	private void sysncTopics() {
		List<TagPO> tagList = tagMapper.select(QueryBuilder.create(TagMapping.class).sort(TagMapping.ID).desc());
		tagList.stream().forEach(vo ->{
			if(StringUtils.isEmpty(vo.getName()) || StringUtils.isEmpty(vo.getName().trim())) {
				return;
			}
			if(TagCategory.personality.getValue().equals(vo.getCategory())) {
				SpringApplicationContext.publishEvent(new TopicCreateEvent(TopicInfo.builder()
						.forceUpate(true)
						.topicId(vo.getId())
						.topicName(vo.getName())
						.topicIcon(vo.getIcon())
						.topicType(Integer.parseInt(BBSTopicType.personalityTag.getValue()))
						.build())
				);
			}else if(TagCategory.statement.getValue().equals(vo.getCategory())) {
				SpringApplicationContext.publishEvent(new TopicCreateEvent(TopicInfo.builder()
						.forceUpate(true)
						.topicId(vo.getId())
						.topicName(vo.getName())
						.topicIcon(vo.getIcon())
						.topicType(Integer.parseInt(BBSTopicType.statementTag.getValue()))
						.build())
				);
			}else if(TagCategory.systemTopic.getValue().equals(vo.getCategory())) {
				if(vo.getRelationTo() == null) {
					SpringApplicationContext.publishEvent(new TopicCreateEvent(TopicInfo.builder()
							.forceUpate(true)
							.topicId(vo.getId())
							.topicName(vo.getName())
							.topicIcon(vo.getIcon())
							.topicType(Integer.parseInt(BBSTopicType.systemSuggestTag.getValue()))
							.build())
					);
				}
			}
		});
		
		List<SchoolPO> shoolList =	schoolMapper.select(QueryBuilder.create(SchoolMapping.class).sort(SchoolMapping.ID).desc());
		shoolList.stream().forEach(vo ->{
			SpringApplicationContext.publishEvent(new TopicCreateEvent(TopicInfo.builder()
					.forceUpate(true)
					.topicId(vo.getId())
					.topicName(vo.getAbbreviation())
					.topicType(Integer.parseInt(BBSTopicType.school.getValue()))
					.build())
			);
		});
		
		List<SpecialismPO> specialismList =	specialismMapper.select(QueryBuilder.create(SpecialismMapping.class).sort(SpecialismMapping.ID).desc());
		specialismList.stream().forEach(vo ->{
			SpringApplicationContext.publishEvent(new TopicCreateEvent(TopicInfo.builder()
					.forceUpate(true)
					.topicId(vo.getId())
					.topicName(vo.getName())
					.topicType(Integer.parseInt(BBSTopicType.specialism.getValue()))
					.build())
			);
		});
		
		List<ResearchDomainPO> researchDomainList =	researchDomainMapper.select(QueryBuilder.create(ResearchDomainMapping.class).sort(ResearchDomainMapping.ID).desc());
		researchDomainList.stream().forEach(vo ->{
			SpringApplicationContext.publishEvent(new TopicCreateEvent(TopicInfo.builder()
					.forceUpate(true)
					.topicId(vo.getId())
					.topicName(vo.getName())
					.topicType(Integer.parseInt(BBSTopicType.researchDomain.getValue()))
					.build())
			);
		});
		
		List<DictDataVO> industryList = dictService.findAllFromCache("industry");
		
		industryList.stream().forEach(vo ->{
			SpringApplicationContext.publishEvent(new TopicCreateEvent(TopicInfo.builder()
					.forceUpate(true)
					.topicId(Long.parseLong(vo.getDictValue()))
					.topicName(vo.getDictShowName())
					.topicType(Integer.parseInt(BBSTopicType.industry.getValue()))
					.build())
			);
		});
		
		
		List<DictDataVO> experienceList = dictService.findAllFromCache("experience");
		experienceList.stream().forEach(vo ->{
			SpringApplicationContext.publishEvent(new TopicCreateEvent(TopicInfo.builder()
					.forceUpate(true)
					.topicId(Long.parseLong(vo.getDictValue()))
					.topicName(vo.getDictShowName())
					.topicType(Integer.parseInt(BBSTopicType.projectType.getValue()))
					.build())
			);
		});
	}
}
