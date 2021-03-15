package com.cloudok.uc.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cloudok.bbs.event.CommentCreateEvent;
import com.cloudok.bbs.event.CommentDeleteEvent;
import com.cloudok.bbs.event.ThumbsUpCreateEvent;
import com.cloudok.bbs.event.ThumbsUpDeleteEvent;
import com.cloudok.bbs.mapper.PostMapper;
import com.cloudok.bbs.mapping.PostMapping;
import com.cloudok.bbs.po.PostPO;
import com.cloudok.bbs.service.CommentService;
import com.cloudok.bbs.service.ThumbsUpService;
import com.cloudok.bbs.vo.CommentVO;
import com.cloudok.bbs.vo.ThumbsUpVO;
import com.cloudok.core.event.BusinessEvent;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.service.AbstractService;
import com.cloudok.enums.NotificationType;
import com.cloudok.uc.mapper.NotificationMapper;
import com.cloudok.uc.po.NotificationPO;
import com.cloudok.uc.service.NotificationService;
import com.cloudok.uc.vo.NotificationVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationServiceImpl extends AbstractService<NotificationVO, NotificationPO> implements ApplicationListener<BusinessEvent<?>>, NotificationService {

	private ExecutorService executor = Executors.newFixedThreadPool(10); // 最多同时8个线程并行

	@Autowired
	private PostMapper postMapper;
	
	@Autowired
	private NotificationMapper repository;

	@Autowired
	private CommentService commentService;

	@Autowired
	private ThumbsUpService thumbsUpService;
	
	@Autowired
	public NotificationServiceImpl(NotificationMapper repository) {
		super(repository);
	}
	@Override
	public void removeByPostId(Long postId) {
		repository.removeByPostId(postId);

	}
	@Override
	public void markAsRead(Long memberId, List<String> businessTypeList) {
		repository.markAsRead(memberId,businessTypeList);
		
	}


	private PostPO getPostById(Long id) {
		List<PostPO> postList = this.postMapper.select(QueryBuilder.create(PostMapping.class).and(PostMapping.ID, id).end());
		return CollectionUtils.isEmpty(postList) ? null : postList.get(0);
	}

	private void onThumbsUpDeleteEvent(ThumbsUpDeleteEvent cast) {
		ThumbsUpVO  vo = cast.getEventData();
		if(vo != null) {
			this.repository.deleteByBusinessId(vo.getId(),NotificationType.thumbsUp.getValue());
		}
	}

	private void onThumbsUpCreateEvent(ThumbsUpCreateEvent cast) {
		ThumbsUpVO  vo = this.thumbsUpService.get(cast.getEventData().getId());
		if(vo != null) {
			PostPO post = this.getPostById(vo.getBusinessId());
			NotificationVO n = this.build(vo, post);
			if(n != null) {
				this.create(n);
			}
		}
		
	}

	private void onCommentDeleteEvent(CommentDeleteEvent cast) {
		CommentVO  vo = cast.getEventData();
		if(vo != null) {
			this.repository.deleteByBusinessId(vo.getId(),NotificationType.comment.getValue());
			this.repository.deleteByBusinessId(vo.getId(),NotificationType.replyComment.getValue());
		}
	}

	private void onCommentCreateEvent(CommentCreateEvent cast) {
		CommentVO  vo = this.commentService.get(cast.getEventData().getId());
		if(vo != null) {
			PostPO post = this.getPostById(vo.getPostId());
			List<NotificationVO> n = this.build(vo, post);
			if(!CollectionUtils.isEmpty(n)) {
				this.create(n);
			}
		}
	}
	private List<NotificationVO> build(CommentVO comment, PostPO post) {
		List<NotificationVO> list = new ArrayList<NotificationVO>();
		//新评论消息
		NotificationVO vo = new NotificationVO();
		vo.setBusinessId(comment.getId());
		vo.setBusinessType(Integer.parseInt(NotificationType.comment.getValue()));
		vo.setTitle("新评论");
		vo.setRemark(comment.getContent().substring(0, Math.min(100, comment.getContent().length())));
		if(post.getCreateBy().equals(comment.getCreateBy())) {
			vo.setStatus(1);
		}else {
			vo.setStatus(0);
		}	
		vo.setStatusTs(comment.getCreateTs());
		vo.setCreateBy(comment.getCreateBy());
		vo.setCreateTs(comment.getCreateTs());
		vo.setUpdateBy(comment.getUpdateBy());
		vo.setUpdateTs(comment.getUpdateTs());
		vo.setMemberId(post.getCreateBy());
		list.add(vo);
		
		if(comment.getReplyTo() != null) {
			vo = new NotificationVO();
			vo.setBusinessId(comment.getId());
			vo.setBusinessType(Integer.parseInt(NotificationType.replyComment.getValue()));
			vo.setTitle("评论新回复");
			vo.setRemark(comment.getContent().substring(0, Math.min(100, comment.getContent().length())));
			if(comment.getReplyTo().getId().equals(comment.getCreateBy())) {
				vo.setStatus(1);
			}else {
				vo.setStatus(0);
			}	
			vo.setStatusTs(comment.getCreateTs());
			vo.setCreateBy(comment.getCreateBy());
			vo.setCreateTs(comment.getCreateTs());
			vo.setUpdateBy(comment.getUpdateBy());
			vo.setUpdateTs(comment.getUpdateTs());
			vo.setMemberId(post.getCreateBy());
			list.add(vo);
		}
		
		return list;
	}

	private NotificationVO build(ThumbsUpVO thumbsUp, PostPO post) {
		NotificationVO vo = new NotificationVO();
		vo.setBusinessId(thumbsUp.getId());
		vo.setBusinessType(Integer.parseInt(NotificationType.thumbsUp.getValue()));
		vo.setTitle("新点赞");
		vo.setRemark(post.getContent().substring(0, Math.min(100, post.getContent().length())));
		if(post.getCreateBy().equals(thumbsUp.getCreateBy())) {
			vo.setStatus(1);
		}else {
			vo.setStatus(0);
		}	
		vo.setStatusTs(thumbsUp.getCreateTs());
		vo.setCreateBy(thumbsUp.getCreateBy());
		vo.setCreateTs(thumbsUp.getCreateTs());
		vo.setUpdateBy(thumbsUp.getUpdateBy());
		vo.setUpdateTs(thumbsUp.getUpdateTs());
		vo.setMemberId(post.getCreateBy());
		return vo;
	}

	@Override
	public void onApplicationEvent(BusinessEvent<?> event) {
		executor.submit(() -> {
			Long start = System.currentTimeMillis();
			if (event instanceof CommentCreateEvent || event instanceof CommentDeleteEvent || event instanceof ThumbsUpCreateEvent || event instanceof ThumbsUpDeleteEvent) {
				if (event instanceof CommentCreateEvent) {
					this.onCommentCreateEvent(CommentCreateEvent.class.cast(event));
				}
				if (event instanceof CommentDeleteEvent) {
					this.onCommentDeleteEvent(CommentDeleteEvent.class.cast(event));
				}
				if (event instanceof ThumbsUpCreateEvent) {
					this.onThumbsUpCreateEvent(ThumbsUpCreateEvent.class.cast(event));
				}
				if (event instanceof ThumbsUpDeleteEvent) {
					this.onThumbsUpDeleteEvent(ThumbsUpDeleteEvent.class.cast(event));
				}
				log.debug("用户推荐评分处理，事件为={}，耗时={} mils", event.getClass().getSimpleName(), (System.currentTimeMillis() - start));
			}

		});
	}

	
}
