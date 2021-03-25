package com.cloudok.uc.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cloudok.base.attach.io.AttachRWHandle;
import com.cloudok.base.attach.vo.AttachVO;
import com.cloudok.base.mapping.TopicMapping;
import com.cloudok.base.service.TopicService;
import com.cloudok.base.vo.TopicVO;
import com.cloudok.bbs.service.PostService;
import com.cloudok.bbs.vo.PostVO;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.service.AbstractService;
import com.cloudok.core.vo.Page;
import com.cloudok.uc.dto.SimpleMemberInfo;
import com.cloudok.uc.mapper.MemberMapper;
import com.cloudok.uc.mapper.MemberTopicMapper;
import com.cloudok.uc.mapping.MemberTopicMapping;
import com.cloudok.uc.po.MemberCirclePO;
import com.cloudok.uc.po.MemberTopicPO;
import com.cloudok.uc.po.UpdatePostCountPO;
import com.cloudok.uc.service.MemberService;
import com.cloudok.uc.service.MemberTopicService;
import com.cloudok.uc.vo.MemberTopicVO;
import com.cloudok.uc.vo.MemberVO;
import com.cloudok.uc.vo.TopicDiscoverVO;

@Service
public class MemberTopicServiceImpl extends AbstractService<MemberTopicVO, MemberTopicPO> implements MemberTopicService{

	@Autowired
	private TopicService topicService;
	@Autowired
	private PostService postService;
	@Autowired
	private MemberTopicMapper repository;
	@Autowired
	private MemberMapper memberMapper;
	@Autowired
	private MemberService memberService;
	
	@Autowired
	public MemberTopicServiceImpl(MemberTopicMapper repository) {
		super(repository);
	}

	@Override
	public Page<TopicDiscoverVO> discover(Integer pageNo, Integer pageSize) {
		Page<TopicVO> page = topicService.page(QueryBuilder.create(TopicMapping.class)
				.sort(TopicMapping.LASTUPDATETS).desc()
				.sort(TopicMapping.PEERCOUNT).desc()
				.enablePaging().page(pageNo, pageSize).end());
		Page<TopicDiscoverVO> result = new Page<TopicDiscoverVO>();
		BeanUtils.copyProperties(page, result);
		if(!CollectionUtils.isEmpty(page.getData())) {
			List<TopicDiscoverVO> list = page.getData().stream().map(item -> {
				TopicDiscoverVO vo = new TopicDiscoverVO();
				BeanUtils.copyProperties(item, vo);
				if(item.getLastPostId() != null) {
					vo.setLatestPost(new PostVO(item.getLastPostId()));
				}
				return vo;
			}).collect(Collectors.toList());
			//填充数据
			//1 最新动态数据
			List<Long> postIdList = list.stream().filter(item -> item.getLatestPost() != null).map(item -> item.getLatestPost().getId()).collect(Collectors.toList());
			if(!CollectionUtils.isEmpty(postIdList)) {
				List<PostVO> postList = postService.getBaseInfo(postIdList);
				list.stream().filter(item -> item.getLatestPost() != null).forEach(item ->{
					postList.stream().filter(post -> post.getId().equals(item.getLatestPost().getId())).findAny().ifPresent(post->{
						item.setLatestPost(post);
					});
				});
			}
			//2 更新了n条post
			List<UpdatePostCountPO> updateList = this.repository.getUpdatePostCount(getCurrentUserId(), list.stream().map(item -> item.getId()).collect(Collectors.toList()));
			list.stream().filter(item -> item.getLatestPost() != null).forEach(item ->{
				item.setUpdatePostCount(item.getPostCount()); //默认是当前post数量
				updateList.stream().filter(update -> update.getId().equals(item.getTopicId())).findAny().ifPresent(update ->{
					 item.setUpdatePostCount(update.getCount());
				});
			});
			//3 相关peers
			List<Long> memberIdList = new ArrayList<Long>();
			list.stream().forEach(item ->{
				if(item.getPeerCount() != null && item.getPeerCount() >0) {
					List<MemberCirclePO> memberList = this.memberMapper.getMemberCirclesListV2(getCurrentUserId(), Arrays.asList(getCurrentUserId()), item.getTopicType(), item.getTopicId(), 0, 5);
					if(!CollectionUtils.isEmpty(memberList)) {
						memberIdList.addAll(memberList.stream().map(m -> m.getMemberId()).collect(Collectors.toList()));
						item.setPeersList(memberList.stream().map(m -> new SimpleMemberInfo(m.getMemberId())).collect(Collectors.toList()));
					}
				}
			});
			if(!CollectionUtils.isEmpty(memberIdList)) {
				List<MemberVO> memberList = this.memberService.get(memberIdList.stream().distinct().collect(Collectors.toList()));
				List<Long> attachIdList = memberList.stream().map(item -> item.getAvatar()).filter(item -> item != null).distinct().collect(Collectors.toList());
				List<AttachVO> attachList = !CollectionUtils.isEmpty(attachIdList) ?  AttachRWHandle.sign(attachIdList) : Collections.emptyList();
				 List<SimpleMemberInfo> smList = memberList.stream().map(item -> {
					SimpleMemberInfo sm = new SimpleMemberInfo(item.getId());
					sm.setAvatar(item.getAvatar()); 
					attachList.stream().filter(a -> a.getId().equals(item.getAvatar())).findAny().ifPresent(a ->{
						sm.setAvatarUrl(a.getUrl());
					});
					return sm;
				}).collect(Collectors.toList());
				list.stream().forEach(item ->{
					if(!CollectionUtils.isEmpty(item.getPeersList())) {
						item.getPeersList().stream().forEach( m -> {
							smList.stream().filter(sm -> sm.getId().equals(m.getId())).findAny().ifPresent(sm -> {
								m.setAvatarUrl(sm.getAvatarUrl());
							});
						});
					}
				});
			}
			
			result.setData(list);
		}
		
		return result;
	}

	@Override
	public TopicVO getDetails(Integer topicType, Long topicId) {
		TopicVO topic =  topicService.getDetails(topicType, topicId);
		this.updatePosition(topic);
		return topic;
	}
	
	private void updatePosition(TopicVO topic) {
		MemberTopicVO record = this.get(QueryBuilder.create(MemberTopicMapping.class)
				.and(MemberTopicMapping.MEMBERID, getCurrentUserId())
				.and(MemberTopicMapping.TOPICID, topic.getId()).end());
		if(record == null) {
			record = new MemberTopicVO();
			record.setTopicId(topic.getId());
			record.setMemberId(getCurrentUserId());
			record.setLastPostId(topic.getLastPostId());
			this.create(record);
		}else {
			this.merge(record);
		}
	}
	
}
