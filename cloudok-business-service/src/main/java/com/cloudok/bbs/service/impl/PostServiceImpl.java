package com.cloudok.bbs.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.cloudok.base.attach.vo.AttachVO;
import com.cloudok.bbs.event.CollectCreateEvent;
import com.cloudok.bbs.event.CollectDeleteEvent;
import com.cloudok.bbs.event.PostCreateEvent;
import com.cloudok.bbs.event.PostDeleteEvent;
import com.cloudok.bbs.event.PostUpdateEvent;
import com.cloudok.bbs.event.ThumbsUpDeleteEvent;
import com.cloudok.bbs.mapper.PostMapper;
import com.cloudok.bbs.mapping.CommentMapping;
import com.cloudok.bbs.mapping.PostTopicMapping;
import com.cloudok.bbs.mapping.ThumbsUpMapping;
import com.cloudok.bbs.po.PostPO;
import com.cloudok.bbs.service.CollectService;
import com.cloudok.bbs.service.CommentService;
import com.cloudok.bbs.service.PostService;
import com.cloudok.bbs.service.PostTopicService;
import com.cloudok.bbs.service.ThumbsUpService;
import com.cloudok.bbs.vo.CollectVO;
import com.cloudok.bbs.vo.PostTopicVO;
import com.cloudok.bbs.vo.PostVO;
import com.cloudok.bbs.vo.ThumbsUpVO;
import com.cloudok.core.context.SpringApplicationContext;
import com.cloudok.core.exception.CoreExceptionMessage;
import com.cloudok.core.exception.SystemException;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.query.QueryOperator;
import com.cloudok.core.service.AbstractService;
import com.cloudok.core.vo.Page;
import com.cloudok.enums.CollectType;
import com.cloudok.security.SecurityContextHelper;
import com.cloudok.uc.dto.SimpleMemberInfo;
import com.cloudok.uc.dto.WholeMemberDTO;
import com.cloudok.uc.service.MemberService;

@Service
public class PostServiceImpl extends AbstractService<PostVO, PostPO> implements PostService {

	@Autowired
	private MemberService memberService;

	@Autowired
	private PostTopicService postTopicService;

	@Autowired
	private CommentService commentService;
	
	@Autowired
	private PostMapper repository;
	
	@Autowired
	private CollectService collectService; 
	
	@Autowired
	private ThumbsUpService thumbsUpService;
	 
	@Autowired
	public PostServiceImpl(PostMapper repository) {
		super(repository);
	}

	@Override
	public PostVO create(PostVO d) {
		PostVO v =  super.create(d);
		SpringApplicationContext.publishEvent(new PostCreateEvent(v));
		return v;
	}

	@Override
	public PostPO convert2PO(PostVO d) {
		PostPO po = super.convert2PO(d);
		if (!StringUtils.isEmpty(d.getAttachList())) {
			po.setAttachIds(d.getAttachList().stream().map(item -> item.getId().toString()).distinct()
					.reduce((a, b) -> a + "," + b).get());
		}
		return po;
	}

	@Override
	public PostVO convert2VO(PostPO e) {
		PostVO vo = super.convert2VO(e);
		if (!StringUtils.isEmpty(e.getAttachIds())) {
			List<AttachVO> attachList = Arrays.asList(e.getAttachIds().split(",")).stream().distinct()
					.filter(item -> !StringUtils.isEmpty(item)).map(item -> {
						AttachVO p = new AttachVO();
						p.setId(Long.parseLong(item));
						return p;
					}).collect(Collectors.toList());
			vo.setAttachList(attachList);
		}
		return vo;
	}

	
	@Override
	public void customUpdate(PostVO d) {
		PostVO vo = this.getSampleInfo(d.getId());
		if (vo != null) {
			if (!vo.getCreateBy().equals(SecurityContextHelper.getCurrentUserId())) {
				throw new SystemException(CoreExceptionMessage.NO_PERMISSION);
			}
		}
		this.repository.customUpdate(this.convert2PO(vo));
		SpringApplicationContext.publishEvent(new PostUpdateEvent(vo));
	}
	
	@Override
	public Integer remove(Long pk) {
		PostVO vo = this.getSampleInfo(pk);
		if (vo != null) {
			if (!vo.getCreateBy().equals(SecurityContextHelper.getCurrentUserId())) {
				throw new SystemException(CoreExceptionMessage.NO_PERMISSION);
			}
		}
		SpringApplicationContext.publishEvent(new PostDeleteEvent(vo));
		return super.remove(pk);
	}
	private PostVO getSampleInfo(Long id) {
		PostVO post =  super.get(id);
		return post;
	}
	@Override
	public PostVO get(Long id) {
		PostVO post =  super.get(id);
		post.setComments(commentService.list(QueryBuilder.create(CommentMapping.class).and(CommentMapping.POSTID, id).end()));
		return post;
	}
	@Override
	public List<PostVO> convert2VO(List<PostPO> e) {
		List<PostVO> list = super.convert2VO(e);
		if (!CollectionUtils.isEmpty(list)) {
			List<PostTopicVO> postTopicList = postTopicService
					.list(QueryBuilder.create(PostTopicMapping.class)
							.and(PostTopicMapping.POSTID, QueryOperator.IN,
									list.stream().map(item -> item.getId()).distinct().collect(Collectors.toList()))
							.end());
			if (!CollectionUtils.isEmpty(postTopicList)) {
				Map<Long,List<PostTopicVO>> map = postTopicList.stream().collect(Collectors.groupingBy(PostTopicVO::getPostId));
				list.stream().forEach(item -> {
					List<PostTopicVO> tempList = map.get(item.getId());
					if(!CollectionUtils.isEmpty(tempList)) {
						item.setTopicList(tempList.stream().map(t -> t.getTopic()).collect(Collectors.toList()));
					}
				});
			}
			List<SimpleMemberInfo> simpleList = memberService.getSimpleMemberInfo(list.stream().map(item -> item.getCreateBy()).distinct().collect(Collectors.toList()));
			list.stream().forEach(item -> {
				simpleList.stream().filter(mem -> mem.getId().equals(item.getCreateBy())).findAny().ifPresent(mem -> {
					item.setMember(mem);
				});
			});
		}
		return list;
	}

	@Override
	public Page<PostVO> searchByTopic(List<Long> topicIdList, Integer pageNo, Integer pageSize) {
		Page<PostVO> page=new Page<>();
		page.setTotalCount(repository.searchByTopicCount(topicIdList));
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		if (page.getTotalCount() > 0 && (page.getTotalCount() / pageSize + 1) >= pageNo) {
			page.setData(this.convert2VO(repository.searchByTopic(topicIdList,(pageNo-1)*pageSize,pageNo*pageSize)));
		}
		return page;
	}
	@Override
	public Page<PostVO> getMyCollectPosts(Long currentUserId, Integer pageNo, Integer pageSize) {
		Page<PostVO> page=new Page<>();
		page.setTotalCount(repository.getMyCollectPostsCount(currentUserId));
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		if (page.getTotalCount() > 0 && (page.getTotalCount() / pageSize + 1) >= pageNo) {
			page.setData(this.convert2VO(repository.getMyCollectPosts(currentUserId,(pageNo-1)*pageSize,pageNo*pageSize)));
		}
		return page;
	}

	@Override
	public Boolean thumbsUp(Long id) {
		List<ThumbsUpVO> list = thumbsUpService.list(QueryBuilder.create(ThumbsUpMapping.class)
				.and(ThumbsUpMapping.CREATEBY, SecurityContextHelper.getCurrentUserId())
				.and(ThumbsUpMapping.BUSINESSID, id).end());
			if(CollectionUtils.isEmpty(list)) {
				CollectVO vo = new CollectVO();
				vo.setBusinessId(id);
				vo.setBusinessType(Integer.parseInt(CollectType.post.getValue()));
				collectService.create(vo);
				SpringApplicationContext.publishEvent(new CollectCreateEvent(vo));
				return true;
			}else {
				return false;
			}
	}

	@Override
	public Boolean cancelThumbsUp(Long id) {
		List<ThumbsUpVO> list = thumbsUpService.list(QueryBuilder.create(ThumbsUpMapping.class)
				.and(ThumbsUpMapping.CREATEBY, SecurityContextHelper.getCurrentUserId())
				.and(ThumbsUpMapping.BUSINESSID, id).end());
			if(!CollectionUtils.isEmpty(list)) {
				thumbsUpService.remove(list.stream().map(item -> item.getId()).collect(Collectors.toList()));
				SpringApplicationContext.publishEvent(new ThumbsUpDeleteEvent(list.get(0)));
				return true;
			}else {
				return false;
			}
	}

	@Override
	public Boolean collect(Long id) {
		
		List<CollectVO> list = collectService.list(QueryBuilder.create(ThumbsUpMapping.class)
				.and(ThumbsUpMapping.CREATEBY, SecurityContextHelper.getCurrentUserId())
				.and(ThumbsUpMapping.BUSINESSID, id).end());
			if(CollectionUtils.isEmpty(list)) {
				CollectVO vo = new CollectVO();
				vo.setBusinessId(id);
				vo.setBusinessType(Integer.parseInt(CollectType.post.getValue()));
				collectService.create(vo);
				SpringApplicationContext.publishEvent(new CollectCreateEvent(vo));
				return true;
			}else {
				return false;
			}

	}

	@Override
	public Boolean cancelCollect(Long id) {
		List<CollectVO> list = collectService.list(QueryBuilder.create(ThumbsUpMapping.class)
				.and(ThumbsUpMapping.CREATEBY, SecurityContextHelper.getCurrentUserId())
				.and(ThumbsUpMapping.BUSINESSID, id).end());
			if(!CollectionUtils.isEmpty(list)) {
				collectService.remove(list.stream().map(item -> item.getId()).collect(Collectors.toList()));
				SpringApplicationContext.publishEvent(new CollectDeleteEvent(list.get(0)));
				return true;
			}else {
				return false;
			}

	}

}
