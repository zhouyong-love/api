package com.cloudok.uc.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cloudok.base.service.TagService;
import com.cloudok.base.vo.TagVO;
import com.cloudok.bbs.event.PostCreateEvent;
import com.cloudok.bbs.event.PostDeleteEvent;
import com.cloudok.bbs.event.PostUpdateEvent;
import com.cloudok.bbs.mapping.PostMapping;
import com.cloudok.bbs.service.PostService;
import com.cloudok.bbs.vo.PostVO;
import com.cloudok.core.context.SpringApplicationContext;
import com.cloudok.core.event.BusinessEvent;
import com.cloudok.core.exception.CoreExceptionMessage;
import com.cloudok.core.exception.SystemException;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.service.AbstractService;
import com.cloudok.enums.BBSTopicType;
import com.cloudok.enums.MemberProfileType;
import com.cloudok.enums.TaggedType;
import com.cloudok.exception.CloudOKExceptionMessage;
import com.cloudok.security.SecurityContextHelper;
import com.cloudok.uc.event.MemberProfileEvent;
import com.cloudok.uc.mapper.MemberTagsMapper;
import com.cloudok.uc.mapping.MemberTagsMapping;
import com.cloudok.uc.po.MemberTagsPO;
import com.cloudok.uc.service.MemberTagsService;
import com.cloudok.uc.vo.MemberTagsVO;
import com.cloudok.uc.vo.SwitchSNRequest;

@Service
public class MemberTagsServiceImpl extends AbstractService<MemberTagsVO, MemberTagsPO> implements MemberTagsService{

	@Autowired
	private TagService tagService;
	
	@Autowired
	private MemberTagsMapper repository;
	
	@Autowired
	private PostService postService;
	
	@Autowired
	public MemberTagsServiceImpl(MemberTagsMapper repository) {
		super(repository);
	}


	@Override
	public MemberTagsVO createByMember(MemberTagsVO d) {
		List<MemberTagsVO> list = super.list(QueryBuilder.create(MemberTagsMapping.class).and(MemberTagsMapping.TAGID, d.getTag().getId()).and(MemberTagsMapping.MEMBERID, getCurrentUserId()).end());
		if(!CollectionUtils.isEmpty(list)) {
			super.remove(list.stream().map(item->item.getId()).collect(Collectors.toList()));
		}
		d.setMemberId(SecurityContextHelper.getCurrentUserId());
		if(d.getTag().getId() == null) {
			d.getTag().setType(TaggedType.CUSTOM.getValue());
			d.setTag(this.tagService.create(d.getTag()));
		}
		d.setType(Integer.parseInt(TaggedType.CUSTOM.getValue()));
		if(d.getSn() == null || d.getSn() == 0) {
			List<MemberTagsVO> r = this.list(QueryBuilder.create(MemberTagsMapping.class)
					.and(MemberTagsMapping.MEMBERID, SecurityContextHelper.getCurrentUserId()).end());
			if(!CollectionUtils.isEmpty(r)) {
				d.setSn(r.stream().mapToInt(item -> item.getSn()).max().getAsInt()+1);
			}else {
				d.setSn(1);
			}
		}
		MemberTagsVO m = super.create(d);
		SpringApplicationContext.publishEvent(MemberProfileEvent.create(getCurrentUserId(),MemberProfileType.tag,d));
		return m;
	}

	@Override
	public MemberTagsVO updateByMember(MemberTagsVO d) {
		MemberTagsVO vo = this.get(d.getId());
		if (vo != null) {
			if (!vo.getMemberId().equals(SecurityContextHelper.getCurrentUserId())) {
				throw new SystemException(CoreExceptionMessage.NO_PERMISSION);
			}
		}
		if(d.getTag().getId() == null) {
			d.setType(Integer.parseInt(TaggedType.CUSTOM.getValue()));
			d.setTag(this.tagService.create(d.getTag()));;
		}
		d.setType(Integer.parseInt(TaggedType.CUSTOM.getValue()));
		d.setMemberId(SecurityContextHelper.getCurrentUserId());
		if(d.getSn() == null) {
			d.setSn(vo.getSn());
		}
		MemberTagsVO v = super.update(d);
		SpringApplicationContext.publishEvent(MemberProfileEvent.update(getCurrentUserId(),MemberProfileType.tag,d,vo));
		return v;
	}

	@Override
	public Integer remove(Long pk) {
		MemberTagsVO vo = this.get(pk);
		if (vo != null) {
			if (!vo.getMemberId().equals(SecurityContextHelper.getCurrentUserId())) {
				throw new SystemException(CoreExceptionMessage.NO_PERMISSION);
			}
		}else {
			return 0;
		}
		int r =  super.remove(pk);
		SpringApplicationContext.publishEvent(MemberProfileEvent.delete(getCurrentUserId(),MemberProfileType.tag,vo));
		return r;
	}


	@Override
	public MemberTagsPO convert2PO(MemberTagsVO d) {
		MemberTagsPO po = super.convert2PO(d);
		po.setTagId(d.getTag().getId());
		return po;
	}

	@Override
	public MemberTagsVO convert2VO(MemberTagsPO e) {
		MemberTagsVO vo = super.convert2VO(e);
		vo.setTag(new TagVO(e.getTagId()));
		if(vo.getSn()==null) {
			vo.setSn(0);
		}
		return vo;
	}

	@Override
	public List<MemberTagsVO> convert2VO(List<MemberTagsPO> e) {
		List<MemberTagsVO> list = super.convert2VO(e);
		if (!CollectionUtils.isEmpty(list)) {
			List<TagVO> tagList = tagService.get(list.stream().map(item -> item.getTag().getId()).distinct().collect(Collectors.toList()));
			list.stream().forEach(item -> {
				tagList.stream().filter(i -> i.getId().equals(item.getTag().getId())).findAny().ifPresent(i -> {
					item.setTag(i);
				}); 
			});
		}

		return list;
	}

	@Override
	public List<MemberTagsVO> getByMember(Long currentUserId) {
		//只查用户给自己打的标签
		return this.list(QueryBuilder.create(MemberTagsMapping.class)
				.and(MemberTagsMapping.MEMBERID, SecurityContextHelper.getCurrentUserId()).and(MemberTagsMapping.TYPE, TaggedType.CUSTOM.getValue()).end());
	}


	@Override
	public MemberTagsVO getByMember(Long currentUserId, Long id) {
		return this.list(QueryBuilder.create(MemberTagsMapping.class)
				.and(MemberTagsMapping.MEMBERID, SecurityContextHelper.getCurrentUserId()).and(MemberTagsMapping.ID, id).end()).get(0);
	}
	
	@Override
	public Object switchSN(@Valid SwitchSNRequest switchSNRequest) {
		if(switchSNRequest.getSourceId() == null || switchSNRequest.getTargetId() == null) {
			throw new SystemException("数据记录不存在",CloudOKExceptionMessage.DEFAULT_ERROR);
		}
		List<MemberTagsVO> list = this.get(Arrays.asList(switchSNRequest.getSourceId(),switchSNRequest.getTargetId()));
		if(CollectionUtils.isEmpty(list) || list.size() != 2) {
			throw new SystemException("数据记录不存在",CloudOKExceptionMessage.DEFAULT_ERROR);
		}
		MemberTagsVO source = list.get(0);
		MemberTagsVO target = list.get(1);
		int sourceSn = source.getSn();
		int targetSn = target.getSn();
		source.setSn(targetSn);
		target.setSn(sourceSn);
		
		this.merge(source);
		this.merge(target);
		
		return true;
	}

	/**
	 * 这里有bug，事件消费顺序可能导致topic统计有问题，改成post发布后 先强制调用这个
	 * @param event
	 */
	public void onPostChange(BusinessEvent<?> event) {
		if(event.isProcessed(getClass())) {
			return;
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
	}

	private void onPostUpdateEvent(PostUpdateEvent cast) {
		PostVO eventData = cast.getEventData();
		Integer topicType = eventData.getTopicType();
		if(BBSTopicType.systemSuggestTag.getValue().equals(topicType.toString())) { //是系统推荐标签
			//新增新标签关联
			this.addNewPostTag(eventData.getCreateBy(), eventData.getTopicId());
		}
		if(eventData.getOldTopicType()!=null) {
			if(BBSTopicType.systemSuggestTag.getValue().equals(eventData.getOldTopicType().toString())) { //是系统推荐标签
				//删除旧标签关联
				this.removePostTag(eventData.getCreateBy(), eventData.getOldTopicId());
			}
		}
	}

	private void onPostDeleteEvent(PostDeleteEvent cast) {
		Integer topicType = cast.getEventData().getTopicType();
		if(BBSTopicType.systemSuggestTag.getValue().equals(topicType.toString())) { //是系统推荐标签
			//删除旧标签关联
			this.removePostTag(cast.getEventData().getCreateBy(), cast.getEventData().getTopicId());
		}
	}

	private void onPostCreateEvent(PostCreateEvent cast) {
		PostVO post = cast.getEventData();
		if(post.getTopicId().equals(post.getOldTopicId()) && post.getTopicType().equals(post.getOldTopicType())) {
			return;
		}
		if(BBSTopicType.systemSuggestTag.getValue().equals(post.getTopicType().toString())) { //是系统推荐标签
			//新增新标签关联
			this.addNewPostTag(cast.getEventData().getCreateBy(), cast.getEventData().getTopicId());
		}
		
	}
	
	@Override
	public void sysnc(Long memberId, Long topicId) {
		this.addNewPostTag(memberId, topicId);
	}
	
	private void addNewPostTag(Long memberId,Long topicId) {
		List<MemberTagsPO> list = this.repository.select(QueryBuilder.create(MemberTagsMapping.class).and(MemberTagsMapping.MEMBERID, memberId)
				.and(MemberTagsMapping.TAGID, topicId).and(MemberTagsMapping.TYPE, TaggedType.POST.getValue()).end());
		if(CollectionUtils.isEmpty(list)) {
			MemberTagsVO vo = new MemberTagsVO();
			 vo.setMemberId(memberId);
			 vo.setSn(9999);
			 vo.setType(Integer.parseInt(TaggedType.POST.getValue()));
			 vo.setTag(new TagVO(topicId));
			 this.create(vo);
		}else {
			if(list.size()>1) {
				 this.repository.delete(list.stream().map(item -> item.getId()).skip(1).collect(Collectors.toList()));
			}
			MemberTagsPO target = list.get(0);
			//触发一下更新时间
			this.merge(this.convert2VO(target));
		}
	}
	
	private void removePostTag(Long memberId,Long topicId) {
		List<MemberTagsPO> list = this.repository.select(QueryBuilder.create(MemberTagsMapping.class).and(MemberTagsMapping.MEMBERID, memberId)
				.and(MemberTagsMapping.TAGID, topicId).and(MemberTagsMapping.TYPE, TaggedType.POST.getValue()).end());
		if(!CollectionUtils.isEmpty(list)) {
			Long count  = this.postService.count(QueryBuilder.create(PostMapping.class).and(PostMapping.CREATEBY, memberId)
					.and(PostMapping.topicType, BBSTopicType.systemSuggestTag.getValue()).and(PostMapping.topicId, topicId).end());
			if(count == null || count.compareTo(0L)==0) {
				 this.repository.delete(list.stream().map(item -> item.getId()).collect(Collectors.toList()));
			}
		}
	}
	
}
