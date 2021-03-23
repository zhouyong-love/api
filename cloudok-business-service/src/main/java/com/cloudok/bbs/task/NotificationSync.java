//package com.cloudok.bbs.task;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//import java.util.stream.Collectors;
//
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.util.CollectionUtils;
//
//import com.cloudok.bbs.mapper.PostMapper;
//import com.cloudok.bbs.mapping.CommentMapping;
//import com.cloudok.bbs.mapping.PostMapping;
//import com.cloudok.bbs.mapping.ThumbsUpMapping;
//import com.cloudok.bbs.po.PostPO;
//import com.cloudok.bbs.service.CommentService;
//import com.cloudok.bbs.service.ThumbsUpService;
//import com.cloudok.bbs.vo.CommentVO;
//import com.cloudok.bbs.vo.ThumbsUpVO;
//import com.cloudok.core.query.QueryBuilder;
//import com.cloudok.core.query.QueryOperator;
//import com.cloudok.enums.NotificationType;
//import com.cloudok.uc.mapping.NotificationMapping;
//import com.cloudok.uc.service.NotificationService;
//import com.cloudok.uc.vo.NotificationVO;
//
//@Component
//public class NotificationSync implements InitializingBean {
//
//	@Autowired
//	private NotificationService notificationService;
//
//	@Autowired
//	private CommentService commentService;
//
//	@Autowired
//	private ThumbsUpService thumbsUpService;
//	
//	@Autowired
//	private PostMapper postMapper;
//
//	@Override
//	public void afterPropertiesSet() throws Exception {
//		new Thread(()->{
//			try {
//				Thread.sleep(TimeUnit.MINUTES.toMillis(1));
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			List<NotificationVO> list = notificationService.list(QueryBuilder.create(NotificationMapping.class).enablePaging().page(1, 1).end());
//				if (CollectionUtils.isEmpty(list)) {
//				this.syncCommentNotificatoin();
//				this.syncthumbsUpNotificatoin();
//			}
//		}).start();
//		
//	}
//
//	private void syncCommentNotificatoin() {
//		int pageNo = 1;
//		QueryBuilder builder = QueryBuilder.create(CommentMapping.class).sort(CommentMapping.ID).desc().enablePaging().page(pageNo, 100).end();
//		List<CommentVO> list = this.commentService.list(builder);
//		
//		while(!CollectionUtils.isEmpty(list)) {
//			List<Long> postIdList = list.stream().map(item -> item.getPostId()).distinct().collect(Collectors.toList());
//			List<PostPO> postList = this.postMapper.select(QueryBuilder.create(PostMapping.class).and(PostMapping.ID, QueryOperator.IN,postIdList ).end());
//			List<NotificationVO> notificatoinList = new ArrayList<NotificationVO>();
//			list.stream().forEach(item -> {
//				postList.stream().filter(post -> post.getId().equals(item.getPostId())).findAny().ifPresent(post -> {
//					NotificationVO  vo = this.build(item, post);
//					notificatoinList.add(vo);
//				});
//			});
//			if(!CollectionUtils.isEmpty(notificatoinList)) {
//				this.notificationService.create(notificatoinList);
//			}
//			 pageNo = pageNo + 1;
//			 builder = builder.enablePaging().page(pageNo, 100).end();
//			 list = this.commentService.list(builder);
//		}
//	}
//
//	private NotificationVO build(CommentVO comment,PostPO post) {
//		NotificationVO vo = new NotificationVO();
//		vo.setBusinessId(comment.getId());
//		vo.setBusinessType(Integer.parseInt(NotificationType.comment.getValue()));
//		vo.setTitle("新评论");
//		vo.setRemark(comment.getContent().substring(0,Math.min(100, comment.getContent().length())));
//		vo.setStatus(comment.getStatus());
//		vo.setStatusTs(comment.getStatusTs());
//		vo.setCreateBy(comment.getCreateBy());
//		vo.setCreateTs(comment.getCreateTs());
//		vo.setUpdateBy(comment.getUpdateBy());
//		vo.setUpdateTs(comment.getUpdateTs());
//		vo.setMemberId(post.getCreateBy());
//		return vo;
//	}
//	
//
//	private NotificationVO build(ThumbsUpVO thumbsUp,PostPO post) {
//		NotificationVO vo = new NotificationVO();
//		vo.setBusinessId(thumbsUp.getId());
//		vo.setBusinessType(Integer.parseInt(NotificationType.thumbsUp.getValue()));
//		vo.setTitle("新点赞");
//		vo.setRemark(post.getContent().substring(0,Math.min(100, post.getContent().length())));
//		vo.setStatus(thumbsUp.getStatus());
//		vo.setStatusTs(thumbsUp.getStatusTs());
//		vo.setCreateBy(thumbsUp.getCreateBy());
//		vo.setCreateTs(thumbsUp.getCreateTs());
//		vo.setUpdateBy(thumbsUp.getUpdateBy());
//		vo.setUpdateTs(thumbsUp.getUpdateTs());
//		vo.setMemberId(post.getCreateBy());
//		return vo;
//	}
//	
//	private void syncthumbsUpNotificatoin() {
//		int pageNo = 1;
//		QueryBuilder builder = QueryBuilder.create(ThumbsUpMapping.class).sort(ThumbsUpMapping.ID).desc().enablePaging().page(pageNo, 100).end();
//		List<ThumbsUpVO> list = this.thumbsUpService.list(builder);
//		while(!CollectionUtils.isEmpty(list)) {
//			List<Long> postIdList = list.stream().map(item -> item.getBusinessId()).distinct().collect(Collectors.toList());
//			List<PostPO> postList = this.postMapper.select(QueryBuilder.create(PostMapping.class).and(PostMapping.ID, QueryOperator.IN,postIdList ).end());
//			List<NotificationVO> notificatoinList = new ArrayList<NotificationVO>();
//			list.stream().forEach(item -> {
//				postList.stream().filter(post -> post.getId().equals(item.getBusinessId())).findAny().ifPresent(post -> {
//					NotificationVO  vo = this.build(item, post);
//					notificatoinList.add(vo);
//				});
//			});
//			if(!CollectionUtils.isEmpty(notificatoinList)) {
//				this.notificationService.create(notificatoinList);
//			}
//			 pageNo = pageNo + 1;
//			 builder = builder.enablePaging().page(pageNo, 100).end();
//			 list = this.thumbsUpService.list(builder);
//		}
//	}
//
//}
