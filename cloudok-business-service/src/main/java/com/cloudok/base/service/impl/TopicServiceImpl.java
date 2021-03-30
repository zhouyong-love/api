package com.cloudok.base.service.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cloudok.base.dict.enums.EnumInfo;
import com.cloudok.base.event.TopicCreateEvent;
import com.cloudok.base.mapper.TopicMapper;
import com.cloudok.base.mapping.TopicMapping;
import com.cloudok.base.po.TopicPO;
import com.cloudok.base.service.TopicService;
import com.cloudok.base.vo.TopicInfo;
import com.cloudok.base.vo.TopicVO;
import com.cloudok.bbs.event.PostCreateEvent;
import com.cloudok.bbs.event.PostDeleteEvent;
import com.cloudok.bbs.event.PostUpdateEvent;
import com.cloudok.bbs.mapper.PostMapper;
import com.cloudok.bbs.mapping.PostMapping;
import com.cloudok.bbs.po.PostPO;
import com.cloudok.bbs.vo.PostVO;
import com.cloudok.core.event.BusinessEvent;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.service.AbstractService;
import com.cloudok.enums.ActionType;
import com.cloudok.enums.BBSTopicType;
import com.cloudok.enums.TagCategory;
import com.cloudok.uc.event.MemberProfileEvent;
import com.cloudok.uc.vo.EducationExperienceVO;
import com.cloudok.uc.vo.InternshipExperienceVO;
import com.cloudok.uc.vo.MemberTagsVO;
import com.cloudok.uc.vo.ProjectExperienceVO;
import com.cloudok.uc.vo.ResearchExperienceVO;

@Service
public class TopicServiceImpl extends AbstractService<TopicVO, TopicPO> implements TopicService, ApplicationListener<BusinessEvent<?>> {

	@Autowired
	private TopicMapper repository;

	@Autowired
	private PostMapper postMapper;

	@Autowired
	public TopicServiceImpl(TopicMapper repository) {
		super(repository);
	}

	@Override
	public TopicVO getDetails(Integer topicType, Long topicId) {
		return this.get(QueryBuilder.create(TopicMapping.class).and(TopicMapping.TOPICTYPE, topicType).and(TopicMapping.TOPICID, topicId).end());
	}
	
	@Override
	public void onApplicationEvent(BusinessEvent<?> event) {
		if(event.isProcessed(getClass())) {
			return;
		}
		// 用户数据更新，动态发布，标签新增都影响数据
		// 0 动态标签 1 研究领域 2 行业 3 社团 4 个性 5状态标签 6 学校 7 专业
		// 动态发布、修改、删除
		if (event instanceof MemberProfileEvent) {
			event.logDetails();
			this.onMemberProfileEvent(MemberProfileEvent.class.cast(event));
		}
		if (event instanceof PostCreateEvent) {
			event.logDetails();
			this.onPostCreateEvent(PostCreateEvent.class.cast(event));
		}
		if (event instanceof PostUpdateEvent) {
			event.logDetails();
			this.onPostUpdateEvent(PostUpdateEvent.class.cast(event));
		}
		if (event instanceof PostDeleteEvent) {
			event.logDetails();
			this.onPostDeleteEvent(PostDeleteEvent.class.cast(event));
		}
		if (event instanceof TopicCreateEvent) {
			event.logDetails();
			TopicCreateEvent target = TopicCreateEvent.class.cast(event);
			TopicInfo topicInfo = target.getEventData();
			TopicVO topic = this.createOrGetTopic(topicInfo.getTopicType(), topicInfo.getTopicId(), topicInfo.getTopicName(), topicInfo.getTopicIcon());
			if (topicInfo.getForceUpate() != null && topicInfo.getForceUpate()) {
				this.repository.updatePeersCount(topic.getId(), topic.getTopicType(), topic.getTopicId());
				PostPO post = this.getLatestPost(topic.getTopicType(), topic.getTopicId());
				this.repository.updatePostCount(topic.getId(), topic.getTopicType(), topic.getTopicId(), post == null ? null : post.getId(),
						post == null ? null : post.getCreateTs());
			}
		}
	}

	private void onPostDeleteEvent(PostDeleteEvent event) {
		PostVO eventData = event.getEventData();
		Long topicId = eventData.getTopicId();
		String topicName = eventData.getTopicName();
		String topicIcon = eventData.getTopicIcon();
		Integer topicType = eventData.getTopicType();
		TopicVO topic = this.createOrGetTopic(topicType, topicId, topicName, topicIcon);
		PostPO post = this.getLatestPost(topic.getTopicType(), topic.getTopicId());
		Timestamp lastUpdateTs  = post == null ? null : post.getCreateTs();
//		if(lastUpdateTs == null) {
//			TopicPO d = this.repository.getTopicInfo(topicType, topicId);
//			if(d != null) {
//				lastUpdateTs = d.getCreateTs();
//			}
//		}
		this.repository.updatePostCount(topic.getId(), topicType, topicId, post == null ? null : post.getId(), lastUpdateTs);
		this.repository.updatePeersCount(topic.getId(), topicType, topicId);
	}

	private void onPostUpdateEvent(PostUpdateEvent event) {
		PostVO eventData = event.getEventData();
		//更新了topic 才更新这个数据
		if (!eventData.getTopicId().equals(eventData.getOldTopicId()) || !eventData.getTopicType().equals(eventData.getOldTopicType())) {
			Long topicId = eventData.getTopicId();
			String topicName = eventData.getTopicName();
			String topicIcon = eventData.getTopicIcon();
			Integer topicType = eventData.getTopicType();
			TopicVO topic = this.createOrGetTopic(topicType, topicId, topicName, topicIcon);
			if (eventData.getCreateTs() == null) {
				eventData.setCreateTs(new Timestamp(System.currentTimeMillis()));
			}
			this.repository.updatePostCount(topic.getId(), topicType, topicId, eventData.getId(), eventData.getCreateTs());
			this.repository.updatePeersCount(topic.getId(), topicType, topicId);
			topicId = eventData.getOldTopicId();
			topicType = eventData.getOldTopicType();
			topic = this.createOrGetTopic(topicType, topicId);
			PostPO post = this.getLatestPost(topic.getTopicType(), topic.getTopicId());
			Timestamp lastUpdateTs  = post == null ? null : post.getCreateTs();
//			if(lastUpdateTs == null) {
//				TopicPO d = this.repository.getTopicInfo(topicType, topicId);
//				if(d != null) {
//					lastUpdateTs = d.getCreateTs();
//				}
//			}
			this.repository.updatePostCount(topic.getId(), topicType, topicId, post == null ? null : post.getId(), lastUpdateTs);
			this.repository.updatePeersCount(topic.getId(), topicType, topicId);
		}

	}

	private void onPostCreateEvent(PostCreateEvent event) {
		PostVO eventData = event.getEventData();
		Long topicId = eventData.getTopicId();
		String topicName = eventData.getTopicName();
		String topicIcon = eventData.getTopicIcon();
		Integer topicType = eventData.getTopicType();
		TopicVO topic = this.createOrGetTopic(topicType, topicId, topicName, topicIcon);
		if (eventData.getCreateTs() == null) {
			eventData.setCreateTs(new Timestamp(System.currentTimeMillis()));
		}
		this.repository.updatePostCount(topic.getId(), topicType, topicId, eventData.getId(), eventData.getCreateTs());
		this.repository.updatePeersCount(topic.getId(), topicType, topicId);
	}

	private void onMemberProfileEvent(MemberProfileEvent event) {
		switch (event.getType()) {
		case association:
			// 无
			break;
		case base:
			// 无
			break;
		case eduction:
			EducationExperienceVO newEduObj = EducationExperienceVO.class.cast(event.getNewObj());
			EducationExperienceVO oldEduObj = event.getOldObj() != null ? EducationExperienceVO.class.cast(event.getOldObj()) : null;
			this.onMemberProfileChange(BBSTopicType.school, event.getActionType(), newEduObj.getSchool().getId(), oldEduObj == null ? null : oldEduObj.getSchool().getId());
			this.onMemberProfileChange(BBSTopicType.specialism, event.getActionType(), newEduObj.getSpecialism().getId(),
					oldEduObj == null ? null : oldEduObj.getSpecialism().getId());
			break;
		case internship:
			InternshipExperienceVO newInternshipObj = InternshipExperienceVO.class.cast(event.getNewObj());
			InternshipExperienceVO oldInternshipObj = event.getOldObj() != null ? InternshipExperienceVO.class.cast(event.getOldObj()) : null;
			Long newCategory = Long.parseLong(newInternshipObj.getIndustry().getCategory());
			Long oldCategory = oldInternshipObj == null ? null : Long.parseLong(oldInternshipObj.getIndustry().getCategory());
			this.onMemberProfileChange(BBSTopicType.industry, event.getActionType(), newCategory, oldCategory);
			break;
		case project:
			ProjectExperienceVO newProjectObj = ProjectExperienceVO.class.cast(event.getNewObj());
			ProjectExperienceVO oldProjectObj = event.getOldObj() != null ? ProjectExperienceVO.class.cast(event.getOldObj()) : null;
			Long newProjectCategory = Long.parseLong(newProjectObj.getCategory());
			Long oldProjectCategory = oldProjectObj == null ? null : Long.parseLong(oldProjectObj.getCategory());
			this.onMemberProfileChange(BBSTopicType.projectType, event.getActionType(), newProjectCategory, oldProjectCategory);
			break;
		case research:
			ResearchExperienceVO newResearchObj = ResearchExperienceVO.class.cast(event.getNewObj());
			ResearchExperienceVO oldResearchObj = event.getOldObj() != null ? ResearchExperienceVO.class.cast(event.getOldObj()) : null;
			Long newResearchDomainId = newResearchObj.getDomain().getId();
			Long oldResearchDomainId = oldResearchObj == null ? null : oldResearchObj.getDomain().getId();
			this.onMemberProfileChange(BBSTopicType.researchDomain, event.getActionType(), newResearchDomainId, oldResearchDomainId);
			break;
		case tag:
			MemberTagsVO newTagObj = MemberTagsVO.class.cast(event.getNewObj());
			MemberTagsVO oldTagObj = event.getOldObj() != null ? MemberTagsVO.class.cast(event.getOldObj()) : null;
			Long newTagId = newTagObj.getTag().getId();
			Long oldTagId = oldTagObj == null ? null : oldTagObj.getTag().getId();
			if (TagCategory.personality.getValue().equals(newTagObj.getTag().getCategory())) {
				this.onMemberProfileChange(BBSTopicType.personalityTag, event.getActionType(), newTagId, oldTagId);
			}
			if (TagCategory.statement.getValue().equals(newTagObj.getTag().getCategory())) {
				this.onMemberProfileChange(BBSTopicType.statementTag, event.getActionType(), newTagId, oldTagId);
			}
			if (TagCategory.systemTopic.getValue().equals(newTagObj.getTag().getCategory())) {
				this.onMemberProfileChange(BBSTopicType.systemSuggestTag, event.getActionType(), newTagId, oldTagId);
			}
			break;

		default:
			break;
		}
	}

	private void onMemberProfileChange(EnumInfo topicType, ActionType actionType, long newTopicId, Long oldTopicId) {
		TopicVO newTopicVO = this.createOrGetTopic(topicType, newTopicId);
		switch (actionType) {
		case CREATE:
			this.repository.updatePeersCount(newTopicVO.getId(), Integer.parseInt(topicType.getValue()), newTopicId);
			break;
		case UPDATE:
			TopicVO oldTopicVO = this.createOrGetTopic(topicType, oldTopicId);
			this.repository.updatePeersCount(newTopicVO.getId(), Integer.parseInt(topicType.getValue()), newTopicId);
			this.repository.updatePeersCount(oldTopicVO.getId(), Integer.parseInt(topicType.getValue()), oldTopicId);
			break;
		case DELETE:
			this.repository.updatePeersCount(newTopicVO.getId(), Integer.parseInt(topicType.getValue()), newTopicId);
			break;
		default:
			break;
		}

	}

	private TopicVO createOrGetTopic(EnumInfo topicType, long topicId) {
		return this.createOrGetTopic(Integer.parseInt(topicType.getValue()), topicId);
	}

	private TopicVO createOrGetTopic(Integer topicType, Long topicId) {
		List<TopicVO> newList = this.list(QueryBuilder.create(TopicMapping.class).and(TopicMapping.TOPICTYPE, topicType).and(TopicMapping.TOPICID, topicId).end());
		if (CollectionUtils.isEmpty(newList)) {
			TopicPO d = this.repository.getTopicInfo(topicType, topicId);
			TopicVO topic = new TopicVO();
			topic.setLastUpdateTs(null);
			topic.setPeerCount(0);
			topic.setPostCount(0);
			topic.setTopicId(topicId);
			topic.setTopicIcon(d.getTopicIcon());
			topic.setTopicType(topicType);
			topic.setTopicName(d.getTopicName());
			return this.create(topic);
		}
		if (newList.size() > 1) {
			this.remove(newList.stream().skip(1).map(item -> item.getId()).collect(Collectors.toList()));
		}
		return newList.get(0);
	}

	private TopicVO createOrGetTopic(Integer topicType, long topicId, String topicName, String topicIcon) {
		List<TopicVO> newList = this.list(QueryBuilder.create(TopicMapping.class).and(TopicMapping.TOPICTYPE, topicType).and(TopicMapping.TOPICID, topicId).end());
		if (CollectionUtils.isEmpty(newList)) {
			TopicVO topic = new TopicVO();
			topic.setPeerCount(0);
			topic.setPostCount(0);
			topic.setTopicId(topicId);
			topic.setTopicIcon(topicIcon);
			topic.setTopicType(topicType);
			topic.setTopicName(topicName);
			return this.create(topic);
		}
		if (newList.size() > 1) {
			this.remove(newList.stream().skip(1).map(item -> item.getId()).collect(Collectors.toList()));
		}
		return newList.get(0);
	}
 

	private PostPO getLatestPost(Integer topicType, Long topicId) {
		List<PostPO> list = this.postMapper.select(QueryBuilder.create(PostMapping.class).and(PostMapping.topicType, topicType).and(PostMapping.topicId, topicId).end()
				.sort(PostMapping.CREATETIME).desc().enablePaging().page(1, 1).end());
		return CollectionUtils.isEmpty(list) ? null : list.get(0);
	}

}
