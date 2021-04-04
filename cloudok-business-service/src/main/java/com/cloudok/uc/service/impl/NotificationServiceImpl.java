package com.cloudok.uc.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.cloudok.base.attach.io.AttachRWHandle;
import com.cloudok.base.attach.vo.AttachVO;
import com.cloudok.bbs.event.CommentCreateEvent;
import com.cloudok.bbs.event.CommentDeleteEvent;
import com.cloudok.bbs.event.ThumbsUpCreateEvent;
import com.cloudok.bbs.event.ThumbsUpDeleteEvent;
import com.cloudok.bbs.mapper.PostMapper;
import com.cloudok.bbs.mapping.PostMapping;
import com.cloudok.bbs.po.BBSNotificationPO;
import com.cloudok.bbs.po.PostPO;
import com.cloudok.bbs.service.CommentService;
import com.cloudok.bbs.service.ThumbsUpService;
import com.cloudok.bbs.vo.BBSNotificationVO;
import com.cloudok.bbs.vo.CommentVO;
import com.cloudok.bbs.vo.ThumbsUpVO;
import com.cloudok.core.event.BusinessEvent;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.query.QueryOperator;
import com.cloudok.core.service.AbstractService;
import com.cloudok.core.vo.Page;
import com.cloudok.enums.NotificationType;
import com.cloudok.uc.dto.SimpleMemberInfo;
import com.cloudok.uc.mapper.NotificationMapper;
import com.cloudok.uc.mapping.NotificationMapping;
import com.cloudok.uc.po.NotificationPO;
import com.cloudok.uc.po.NotificationTotalPO;
import com.cloudok.uc.service.MemberService;
import com.cloudok.uc.service.NotificationService;
import com.cloudok.uc.vo.NotificationTotalVO;
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
	private MemberService memberService;
	
	@Autowired
	public NotificationServiceImpl(NotificationMapper repository) {
		super(repository);
	}
	@Override
	public void removeByPostId(Long postId) {
		repository.removeByPostId(postId);

	}
//	@Override
//	public void markAsRead(Long memberId, List<String> businessTypeList) {
//		repository.markAsRead(memberId,businessTypeList);
//		
//	}
	@Override
	public void markAsRead(List<Long> idList) {
		repository.markAsReadByIdList(idList);
		
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
		
		if(comment.getReplyTo() != null ) {
			//动态发布人和RE人是同一个人，则不再提醒
			if(!comment.getReplyTo().getId().equals(post.getCreateBy())) {
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
				vo.setMemberId(comment.getReplyTo().getId());  //回复某人则提醒某人
				list.add(vo);
			}
			
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
		if(event.isProcessed(getClass())) {
			return;
		}
		if (event instanceof CommentCreateEvent || event instanceof CommentDeleteEvent || event instanceof ThumbsUpCreateEvent || event instanceof ThumbsUpDeleteEvent) {
			event.logDetails();
			executor.submit(() -> {
				Long start = System.currentTimeMillis();
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
			});
		}
		
	}
	@Override
	public List<NotificationTotalVO> getTotal() {
		List<NotificationTotalPO> list = this.repository.getTotal(getCurrentUserId());
		List<NotificationTotalVO> result = new ArrayList<NotificationTotalVO>();
		//目前只有1，2,3 其中1，2 为回复，3为点赞
		NotificationTotalVO  comment = NotificationTotalVO.builder().businessType(1).totalCount(0).unReadCount(0).build();
		NotificationTotalVO  thumbsUp =  NotificationTotalVO.builder().businessType(2).totalCount(0).unReadCount(0).build();
		if(!CollectionUtils.isEmpty(list)) {
			list.stream().forEach(po -> {
				if(po.getBusinessType().equals(1) || po.getBusinessType().equals(3)) {
					comment.setTotalCount(comment.getTotalCount()+po.getCount());
					if(po.getStatus().equals(0)) {
						comment.setUnReadCount(po.getCount()+comment.getUnReadCount());
					}
				}
				if(po.getBusinessType().equals(2)) {
					thumbsUp.setTotalCount(thumbsUp.getTotalCount()+po.getCount());
					if(po.getStatus().equals(0)) {
						thumbsUp.setUnReadCount(po.getCount()+thumbsUp.getUnReadCount());
					}
				}
			});
			if(comment.getTotalCount()>0) {
				comment.setLatestMemberList(this.memberService.getSimpleMemberInfo(this.getLatestMember(getCurrentUserId(), Arrays.asList(1,3), 3)));
			}
			if(thumbsUp.getTotalCount()>0) {
				thumbsUp.setLatestMemberList(this.memberService.getSimpleMemberInfo(this.getLatestMember(getCurrentUserId(), Arrays.asList(2), 3)));
			}
			result.add(comment);
			result.add(thumbsUp);
		}
		return result;
	}
	
	private List<Long> getLatestMember(Long currentUserId,List<Integer> typeList,int maxSize){
		List<Long> idList = new ArrayList<Long>();
		int pageNo = 1;
		int pageSize = 10;
		List<NotificationPO> list = this.repository.select(QueryBuilder.create(NotificationMapping.class)
				.and(NotificationMapping.BUSINESSTYPE,QueryOperator.IN, typeList)
				.and(NotificationMapping.MEMBERID, getCurrentUserId())
				.end().sort(NotificationMapping.CREATETIME).desc().enablePaging().page(pageNo, pageSize).end()
				);
		while(!CollectionUtils.isEmpty(list)) {
			list.stream().forEach(item ->{
				if(idList.size()<maxSize && !idList.contains(item.getCreateBy())) {
					idList.add(item.getCreateBy());
				}
			});
			if(idList.size()>=maxSize) {
				break;
			}
			if(list.size()<pageSize) {
				break;
			}
			pageNo = pageNo + 1;
			list = this.repository.select(QueryBuilder.create(NotificationMapping.class)
					.and(NotificationMapping.BUSINESSTYPE,QueryOperator.IN, typeList)
					.and(NotificationMapping.MEMBERID, getCurrentUserId())
					.end().sort(NotificationMapping.CREATETIME).desc().enablePaging().page(pageNo, pageSize).end()
					);
		}
		return idList;
		
	}
	@Override
	public Page<BBSNotificationVO> getNotificationList(Integer type, Integer pageNo, Integer pageSize) {
			Page<BBSNotificationVO> page = new Page<>();
			page.setTotalCount(postMapper.getNotificationCountByType(getCurrentUserId(),type));
			page.setPageNo(pageNo);
			page.setPageSize(pageSize);
			if (page.getTotalCount() > 0 && (page.getTotalCount() / pageSize + 1) >= pageNo) {
				List<BBSNotificationPO> list = postMapper.getNotificationListByType(getCurrentUserId(),type, Math.max(pageNo - 1, 0) * pageSize, pageSize);
				List<BBSNotificationVO> voList = list.stream().map(item -> {
					BBSNotificationVO vo = new BBSNotificationVO();
					BeanUtils.copyProperties(item, vo);
					if (!StringUtils.isEmpty(item.getAttachIds())) {
						List<Long> attachList = Arrays.asList(item.getAttachIds().split(",")).stream().distinct().filter(a -> !StringUtils.isEmpty(a)).map(a -> Long.parseLong(a))
								.collect(Collectors.toList());
						if (!CollectionUtils.isEmpty(attachList)) {
							vo.setPhoto(new AttachVO(attachList.get(0))); // 取第一个图
						}
					}
					vo.setMemberInfo(new SimpleMemberInfo(item.getMemberId()));
					return vo;
				}).collect(Collectors.toList());
				// 填充memberInfo 和 图片数据
				List<Long> memberIdList = voList.stream().filter(item -> item.getMemberInfo() != null).map(item -> item.getMemberInfo().getId()).distinct()
						.collect(Collectors.toList());
				List<Long> attachIdList = voList.stream().filter(item -> item.getPhoto() != null).map(item -> item.getPhoto().getId()).distinct().collect(Collectors.toList());
				List<AttachVO> attachList = AttachRWHandle.sign(attachIdList);
				List<SimpleMemberInfo> memberList = this.memberService.getSimpleMemberInfo(memberIdList);
				voList.stream().forEach(item -> {
					if (item.getMemberInfo() != null) {
						memberList.stream().filter(m -> m.getId().equals(item.getMemberInfo().getId())).findAny().ifPresent(m -> {
							item.setMemberInfo(m);
						});
					}
					if (item.getPhoto() != null) {
						attachList.stream().filter(m -> m.getId().equals(item.getPhoto().getId())).findAny().ifPresent(m -> {
							item.setPhoto(m);
						});
					}
				});
				this.repository.markAsRead(getCurrentUserId(),Arrays.asList(type.toString()));
				page.setData(voList);
			}
			return page;
	}

	
}
