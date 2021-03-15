package com.cloudok.bbs.service.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cloudok.bbs.event.CommentDeleteEvent;
import com.cloudok.bbs.mapper.CommentMapper;
import com.cloudok.bbs.mapping.CommentMapping;
import com.cloudok.bbs.po.CommentPO;
import com.cloudok.bbs.service.CommentService;
import com.cloudok.bbs.service.PostService;
import com.cloudok.bbs.vo.CommentVO;
import com.cloudok.bbs.vo.PostVO;
import com.cloudok.core.context.SpringApplicationContext;
import com.cloudok.core.exception.CoreExceptionMessage;
import com.cloudok.core.exception.SystemException;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.service.AbstractService;
import com.cloudok.core.vo.Page;
import com.cloudok.security.SecurityContextHelper;
import com.cloudok.uc.dto.SimpleMemberInfo;
import com.cloudok.uc.service.MemberService;

@Service
public class CommentServiceImpl extends AbstractService<CommentVO, CommentPO> implements CommentService{

	@Autowired
	private MemberService memberService;
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private CommentMapper repository;
	
	@Autowired
	public CommentServiceImpl(CommentMapper repository) {
		super(repository);
	}
	
	@Override
	public CommentVO create(CommentVO d) {
		PostVO post = this.postService.get(d.getPostId());
		if(post == null) {
			throw new SystemException("动态已经被删除",CoreExceptionMessage.NOTFOUND_ERR);
		}
		if(post.getCreateBy().equals(getCurrentUserId())) {
			d.setStatus(1);
			d.setStatusTs(new Timestamp(System.currentTimeMillis()));
		}else {
			d.setStatus(0);
			d.setStatusTs(new Timestamp(System.currentTimeMillis()));
		}
	
		CommentVO vo =  super.create(d);
		SpringApplicationContext.publishEvent(new CommentDeleteEvent(vo));
		return vo;
	}
	@Override
	public CommentVO update(CommentVO d) {
		PostVO post = this.postService.get(d.getPostId());
		if(post == null) {
			throw new SystemException("动态已经被删除",CoreExceptionMessage.NOTFOUND_ERR);
		}
		CommentVO vo = this.get(d.getId());
		if (vo != null) {
			if (!vo.getCreateBy().equals(SecurityContextHelper.getCurrentUserId())) {
				throw new SystemException(CoreExceptionMessage.NO_PERMISSION);
			}
		}
		return super.update(d);
	}
	
	@Override
	public Integer remove(Long pk) {
		CommentVO vo = this.get(pk);
		if (vo != null) {
			PostVO post = this.postService.get(vo.getPostId());
			if(post == null) {
				throw new SystemException("动态已经被删除",CoreExceptionMessage.NOTFOUND_ERR);
			}
			//检查评论是不是我发布的
			if (!vo.getCreateBy().equals(SecurityContextHelper.getCurrentUserId())) { //不是我发的
				//不是我发的
				if(post == null || !post.getCreateBy().equals(SecurityContextHelper.getCurrentUserId())) {
					throw new SystemException(CoreExceptionMessage.NO_PERMISSION);
				}
			}
		}
		Integer row = super.remove(pk);
		SpringApplicationContext.publishEvent(new CommentDeleteEvent(vo));
		return row;
	}
	
	public void fillMemberInfo(List<CommentVO> list) {
		if (!CollectionUtils.isEmpty(list)) {
			List<SimpleMemberInfo> simpleList =  memberService.getSimpleMemberInfo(list.stream().map(item -> item.getCreateBy()).distinct().collect(Collectors.toList()));
			list.stream().forEach(item -> {
				simpleList.stream().filter(mem -> mem.getId().equals(item.getCreateBy())).findAny().ifPresent(mem -> {
					item.setMemberInfo(mem);
				});
			});
		}
	}

	@Override
	public Page<CommentVO> getCommentList(Long postId,Integer pageNo, Integer pageSize) {
		Page<CommentVO> page = this.page(QueryBuilder.create(CommentMapping.class).and(CommentMapping.POSTID, postId).end()
				.sort(CommentMapping.CREATETIME).desc()
				.enablePaging().page(pageNo, pageSize).end());
		this.fillMemberInfo(page.getData());
		return page;
	}

	@Override
	public List<CommentVO> getMyRecognizedComments(Long currentUserId, List<Long> postIdList, int maxSize) {
		return this.convert2VO(this.repository.getMyRecognizedComments(currentUserId,postIdList,maxSize));
	}
	@Override
	public void markAsRead(List<Long> commentIdList) {
		if(CollectionUtils.isEmpty(commentIdList)) {
			return;
		}
		repository.markAsRead(commentIdList);
		
	}
	@Override
	public void removeByPostId(Long postId) {
		this.repository.removeByPostId(postId);
	}

}
