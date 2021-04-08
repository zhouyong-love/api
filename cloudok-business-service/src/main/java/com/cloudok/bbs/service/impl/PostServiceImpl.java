package com.cloudok.bbs.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.cloudok.base.attach.io.AttachRWHandle;
import com.cloudok.base.attach.vo.AttachVO;
import com.cloudok.base.dict.service.DictService;
import com.cloudok.base.dict.vo.DictDataVO;
import com.cloudok.base.mapping.TagMapping;
import com.cloudok.base.service.TagService;
import com.cloudok.base.vo.TagVO;
import com.cloudok.bbs.event.CollectCreateEvent;
import com.cloudok.bbs.event.CollectDeleteEvent;
import com.cloudok.bbs.event.CommentCreateEvent;
import com.cloudok.bbs.event.CommentDeleteEvent;
import com.cloudok.bbs.event.PostCreateEvent;
import com.cloudok.bbs.event.PostDeleteEvent;
import com.cloudok.bbs.event.PostUpdateEvent;
import com.cloudok.bbs.event.ThumbsUpCreateEvent;
import com.cloudok.bbs.event.ThumbsUpDeleteEvent;
import com.cloudok.bbs.mapper.PostMapper;
import com.cloudok.bbs.mapping.CommentMapping;
import com.cloudok.bbs.mapping.PostMapping;
import com.cloudok.bbs.mapping.ThumbsUpMapping;
import com.cloudok.bbs.po.BBSNotificationPO;
import com.cloudok.bbs.po.PostPO;
import com.cloudok.bbs.service.CollectService;
import com.cloudok.bbs.service.CommentService;
import com.cloudok.bbs.service.PostService;
import com.cloudok.bbs.service.ThumbsUpService;
import com.cloudok.bbs.vo.BBSNotificationVO;
import com.cloudok.bbs.vo.CollectVO;
import com.cloudok.bbs.vo.CommentVO;
import com.cloudok.bbs.vo.PostVO;
import com.cloudok.bbs.vo.ThumbsUpVO;
import com.cloudok.bbs.vo.TopicGroupVO;
import com.cloudok.bbs.vo.TopicVO;
import com.cloudok.bbs.vo.TopticPostResult;
import com.cloudok.core.context.SpringApplicationContext;
import com.cloudok.core.event.BusinessEvent;
import com.cloudok.core.exception.CoreExceptionMessage;
import com.cloudok.core.exception.SystemException;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.query.QueryOperator;
import com.cloudok.core.service.AbstractService;
import com.cloudok.core.vo.Page;
import com.cloudok.enums.BBSTopicType;
import com.cloudok.enums.CollectType;
import com.cloudok.enums.TagCategory;
import com.cloudok.security.SecurityContextHelper;
import com.cloudok.uc.dto.SimpleMemberInfo;
import com.cloudok.uc.dto.WholeMemberDTO;
import com.cloudok.uc.service.MemberService;
import com.cloudok.uc.service.MemberTagsService;
import com.cloudok.uc.service.NotificationService;
import com.cloudok.util.StreamUtils;

@Service
public class PostServiceImpl extends AbstractService<PostVO, PostPO> implements PostService, ApplicationListener<BusinessEvent<?>> {

	@Autowired
	private MemberService memberService;

	@Autowired
	private CommentService commentService;

	@Autowired
	private PostMapper repository;

	@Autowired
	private CollectService collectService;

	@Autowired
	private ThumbsUpService thumbsUpService;

	@Autowired
	private DictService dictService;

	// @Autowired
	// private TagService tagService;
	//
	// @Autowired
	// private ResearchDomainService researchDomainService;
	//
	// @Autowired
	// private SchoolService schoolService;
	//
	// @Autowired
	// private SpecialismService specialismService;
	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private MemberTagsService memberTagsService;

	@Autowired
	public PostServiceImpl(PostMapper repository) {
		super(repository);
	}

	@Override
	public PostVO createByMember(@Valid PostVO vo) {
		vo.setCollectCount(0);
		vo.setCommentCount(0);
		vo.setThumbsUpCount(0);
		super.create(vo);
		BusinessEvent<?> event = new PostCreateEvent(vo);
		memberTagsService.onPostChange(event);
		SpringApplicationContext.publishEvent(event);
		return vo;
	}

	@Override
	public PostVO updateByMember(@Valid PostVO vo) {
		PostVO db = this.getSampleInfo(vo.getId());
		if (db != null) {
			if (!db.getCreateBy().equals(SecurityContextHelper.getCurrentUserId())) {
				throw new SystemException(CoreExceptionMessage.NO_PERMISSION);
			}
		}
		repository.updateByMember(this.convert2PO(vo));
		vo.setOldTopicId(db.getTopicId());
		vo.setOldTopicType(db.getTopicType());
		vo.setCreateBy(db.getCreateBy());
		BusinessEvent<?> event = new PostUpdateEvent(vo);
		memberTagsService.onPostChange(event);
		SpringApplicationContext.publishEvent(event);
		return vo;
	}

	@Override
	public PostPO convert2PO(PostVO d) {
		PostPO po = super.convert2PO(d);
		if (!StringUtils.isEmpty(d.getAttachList())) {
			po.setAttachIds(d.getAttachList().stream().map(item -> item.getId().toString()).distinct().reduce((a, b) -> a + "," + b).get());
		}
		return po;
	}

	@Override
	public PostVO convert2VO(PostPO e) {
		PostVO vo = super.convert2VO(e);
		if (!StringUtils.isEmpty(e.getAttachIds())) {
			List<AttachVO> attachList = Arrays.asList(e.getAttachIds().split(",")).stream().distinct().filter(item -> !StringUtils.isEmpty(item)).map(item -> {
				AttachVO p = new AttachVO();
				p.setId(Long.parseLong(item));
				return p;
			}).collect(Collectors.toList());
			vo.setAttachList(attachList);
		}
		return vo;
	}
 
	private PostVO getSampleInfo(Long id) {
		PostVO post = super.get(id);
		return post;
	}

	@Override
	public PostVO get(Long id) {
		PostVO post = super.get(id);
		post.setCommentList(commentService.list(QueryBuilder.create(CommentMapping.class).and(CommentMapping.POSTID, id).end()));
		return post;
	}

	@Override
	public List<PostVO> convert2VO(List<PostPO> e) {
		List<PostVO> list = super.convert2VO(e);
		if (!CollectionUtils.isEmpty(list)) {
			List<SimpleMemberInfo> simpleList = memberService.getSimpleMemberInfo(list.stream().map(item -> item.getCreateBy()).distinct().collect(Collectors.toList()));
			list.stream().forEach(item -> {
				simpleList.stream().filter(mem -> mem.getId().equals(item.getCreateBy())).findAny().ifPresent(mem -> {
					item.setMemberInfo(mem);
				});
			});
		}
		return list;
	}

	@Override
	public Page<PostVO> getMyCollectPosts(Long currentUserId, Integer pageNo, Integer pageSize) {
		Page<PostVO> page = new Page<>();
		page.setTotalCount(repository.getMyCollectPostsCount(currentUserId));
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		if (page.getTotalCount() > 0 && (page.getTotalCount() / pageSize + 1) >= pageNo) {
			page.setData(this.convert2VO(repository.getMyCollectPosts(currentUserId, (pageNo - 1) * pageSize, pageSize)));
		}
		return page;
	}

	@Override
	public TopticPostResult getPostsByTopic(Long topicId, Integer topicType, Integer pageNo, Integer pageSize) {
		TopticPostResult result = new TopticPostResult();
		Page<PostVO> page = this.page(QueryBuilder.create(PostMapping.class).and(PostMapping.topicId, topicId).and(PostMapping.topicType, topicType).end()
				.sort(PostMapping.CREATETIME).desc().enablePaging().page(pageNo, pageSize).end());
		this.fillPostInfo(page.getData());
		result.setPostPage(page);
		result.setPostCount(page.getTotalCount());
		result.setPeersCount(this.repository.getPeersCount(topicId, topicType));
		return result;
	}
	
	@Override
	public Page<PostVO> getPostCircles(Long topicId, Integer topicType, Integer pageNo, Integer pageSize) {
		Page<PostVO> page = this.page(QueryBuilder.create(PostMapping.class).and(PostMapping.topicId, topicId).and(PostMapping.topicType, topicType).end()
				.sort(PostMapping.CREATETIME).desc().enablePaging().page(pageNo, pageSize).end());
		this.fillPostInfo(page.getData());
		return page;
	}

	@Override
	public Page<PostVO> discover(Integer pageNo, Integer pageSize,Long memberId) {
		Page<PostVO> page = null;
		if(memberId!=null&&memberId>0) {
			page = this.page(QueryBuilder.create(PostMapping.class).and(PostMapping.CREATEBY, memberId).end().sort(PostMapping.CREATETIME).desc().enablePaging().page(pageNo, pageSize).end());
		}else {
			Long discoverCount = repository.discoverCount(getCurrentUserId());
			page = new Page<PostVO>();
			page.setTotalCount(discoverCount);
			page.setPageNo(pageNo);
			page.setPageSize(pageSize);
			if(discoverCount > 0) {
				page.setData(super.convert2VO(repository.discover(getCurrentUserId(), Math.max(pageNo - 1, 0) * pageSize, pageSize)));
			}
		}
		this.fillPostInfo(page.getData());
		return page;
	}
	@Override
	public List<PostVO> getBaseInfo(List<Long> postIdList) {
		List<PostVO> postList =	this.get(postIdList);
		this.fillPostBaseInfo(postList);
		return postList;
	}

	private void fillPostBaseInfo(List<PostVO> postList) {
		if (CollectionUtils.isEmpty(postList)) {
			return;
		}
		// 填充：图片，memberInfo
		List<Long> attachIdList = new ArrayList<Long>();
		List<Long> memberIdList = new ArrayList<Long>();
	 
		postList.stream().forEach(item -> {
			if (!CollectionUtils.isEmpty(item.getAttachList())) {
				attachIdList.addAll(item.getAttachList().stream().map(a -> a.getId()).collect(Collectors.toList()));
			}
			memberIdList.add(item.getCreateBy());
		});
		if (!CollectionUtils.isEmpty(attachIdList)) {
			List<AttachVO> attachList = AttachRWHandle.sign(attachIdList);
			postList.stream().forEach(post -> {
				if (!CollectionUtils.isEmpty(post.getAttachList())) {
					post.getAttachList().stream().forEach(item -> {
						attachList.stream().filter(a -> a.getId().equals(item.getId())).findAny().ifPresent(a -> {
							BeanUtils.copyProperties(a, item);
						});
					});
				}
			});
		}
		List<SimpleMemberInfo> memberList = this.memberService.getSimpleMemberInfo(memberIdList.stream().distinct().collect(Collectors.toList()));
		postList.stream().forEach(item -> {
			memberList.stream().filter(m -> m.getId().equals(item.getCreateBy())).findAny().ifPresent(m -> {
				item.setMemberInfo(m);
			});
		});
	}

	private void fillPostInfo(List<PostVO> postList) {
		if (CollectionUtils.isEmpty(postList)) {
			return;
		}
		// 填充我关注的人最新一条评论
		this.fillPostComments(postList.stream().filter(item -> item.getCommentCount() != null && item.getCommentCount() != 0).collect(Collectors.toList()));
		// 如果我关注的人没有评论这个的，则获取前n个点赞数据，前端显示点赞数据
		this.fillPostThumbsUp(
				postList.stream().filter(item -> item.getThumbsUpCount() != null && item.getThumbsUpCount() != 0 && item.getLatestComment() == null).collect(Collectors.toList()),
				5);
		// 填充：图片，memberInfo，点赞，评论等数据
		List<Long> attachIdList = new ArrayList<Long>();
		List<Long> memberIdList = new ArrayList<Long>();
	 

		postList.stream().forEach(item -> {
			if (!CollectionUtils.isEmpty(item.getAttachList())) {
				attachIdList.addAll(item.getAttachList().stream().map(a -> a.getId()).collect(Collectors.toList()));
			}
			if (item.getLatestComment() != null) {
				memberIdList.add(item.getLatestComment().getCreateBy());
			}
			//最多三个点赞
			if(!CollectionUtils.isEmpty(item.getThumbsUpList())) {
				item.setThumbsUpList(item.getThumbsUpList().stream().limit(5).collect(Collectors.toList()));
			}
			if (!CollectionUtils.isEmpty(item.getThumbsUpList())) {
				memberIdList.addAll(item.getThumbsUpList().stream().map(a -> a.getCreateBy()).collect(Collectors.toList()));
			}
			memberIdList.add(item.getCreateBy());
		});
		if (!CollectionUtils.isEmpty(attachIdList)) {
			List<AttachVO> attachList = AttachRWHandle.sign(attachIdList);
			postList.stream().forEach(post -> {
				if (!CollectionUtils.isEmpty(post.getAttachList())) {
					post.getAttachList().stream().forEach(item -> {
						attachList.stream().filter(a -> a.getId().equals(item.getId())).findAny().ifPresent(a -> {
							BeanUtils.copyProperties(a, item);
						});
					});
				}
			});
		}
		List<SimpleMemberInfo> memberList = this.memberService.getSimpleMemberInfo(memberIdList.stream().distinct().collect(Collectors.toList()));
		postList.stream().forEach(item -> {
			if (item.getLatestComment() != null) {
				memberList.stream().filter(m -> m.getId().equals(item.getLatestComment().getCreateBy())).findAny().ifPresent(m -> {
					item.getLatestComment().setMemberInfo(m);
				});
			}
			if (!CollectionUtils.isEmpty(item.getThumbsUpList())) {
				item.getThumbsUpList().forEach(th -> {
					memberList.stream().filter(m -> m.getId().equals(th.getCreateBy())).findAny().ifPresent(m -> {
						th.setMemberInfo(m);
					});
				});
			}
			memberList.stream().filter(m -> m.getId().equals(item.getCreateBy())).findAny().ifPresent(m -> {
				item.setMemberInfo(m);
			});
		});
		List<Long> idList = postList.stream().filter(item -> item.getMyThumbsUp() == null).map(item -> item.getId()).distinct().collect(Collectors.toList());
		if (!CollectionUtils.isEmpty(idList)) {
			List<ThumbsUpVO> thumbsUpList = this.thumbsUpService.list(
					QueryBuilder.create(ThumbsUpMapping.class).and(ThumbsUpMapping.BUSINESSID, QueryOperator.IN, idList).and(ThumbsUpMapping.CREATEBY, getCurrentUserId()).end());
			if (!CollectionUtils.isEmpty(thumbsUpList)) {
				postList.stream().filter(item -> item.getMyThumbsUp() == null).forEach(item -> {
					item.setMyThumbsUp(false);
					thumbsUpList.stream().filter(th -> th.getBusinessId().equals(item.getId())).findAny().ifPresent(th -> {
						item.setMyThumbsUp(true);
					});
				});
			}
		}
	}

	private void fillPostThumbsUp(List<PostVO> postList, int size) {
		if (CollectionUtils.isEmpty(postList)) {
			return;
		}
		List<Long> postIdList = postList.stream().map(item -> item.getId()).distinct().collect(Collectors.toList());
		// 获取我关注的人的评论，只要有一条，则前端显示第一条，不显示点赞
		List<ThumbsUpVO> thumbsUpList = this.thumbsUpService.list(QueryBuilder.create(ThumbsUpMapping.class).and(ThumbsUpMapping.BUSINESSID, QueryOperator.IN, postIdList).end()
				.sort(ThumbsUpMapping.CREATETIME).desc().enablePaging().page(1, postList.size() * 100).end());
		if (!CollectionUtils.isEmpty(thumbsUpList)) {
			Map<Long, List<ThumbsUpVO>> map = thumbsUpList.stream().collect(Collectors.groupingBy(ThumbsUpVO::getBusinessId));
			postList.stream().forEach(item -> {
				List<ThumbsUpVO> list = map.get(item.getId());
				if (!CollectionUtils.isEmpty(list)) { // 排序
					list.sort((b, a) -> a.getCreateTs().compareTo(b.getCreateTs()));
					item.setThumbsUpList(list);
					item.setMyThumbsUp(false);
					list.stream().filter(th -> th.getCreateBy().equals(getCurrentUserId())).findAny().ifPresent(th -> {
						item.setMyThumbsUp(true);
					});
				}
			});
			if (thumbsUpList.size() == postList.size() * 100) { // 只有还有数据才去取
				postList = postList.stream().filter(item -> {
					if (CollectionUtils.isEmpty(item.getThumbsUpList())) { // 有人点赞，但是数量为空，则要继续取
						return true;
					}
					if (item.getThumbsUpList().size() < item.getThumbsUpCount()) { // 没取完要继续取
						return true;
					}
					return false;
				}).collect(Collectors.toList());
				this.fillPostThumbsUp(postList, size);
			}
		}
	}

	private void fillPostComments(List<PostVO> postList) {
		if (CollectionUtils.isEmpty(postList)) {
			return;
		}
		List<Long> postIdList = postList.stream().map(item -> item.getId()).distinct().collect(Collectors.toList());
		// 获取我关注的人的评论，只要有一条，则前端显示第一条，不显示点赞
		List<CommentVO> commemntList = commentService.getMyRecognizedComments(SecurityContextHelper.getCurrentUserId(), postIdList, 1000);
		if (!CollectionUtils.isEmpty(commemntList)) {
			Map<Long, List<CommentVO>> map = commemntList.stream().collect(Collectors.groupingBy(CommentVO::getPostId));
			postList.stream().forEach(item -> {
				List<CommentVO> list = map.get(item.getId());
				if (!CollectionUtils.isEmpty(list)) { // 排序
					list.sort((b,a) -> a.getCreateTs().compareTo(b.getCreateTs()));
					item.setLatestComment(list.get(0));
				}
			});
			postList = postList.stream().filter(item -> {
				if (item.getLatestComment() == null) { // 有人评论，但是数量为空，则要继续取
					return true;
				}
				return false;
			}).collect(Collectors.toList());
			this.fillPostComments(postList);
		}
	}

	@Override
	public PostVO getDetails(Long id) {
		PostVO post = this.get(id);
		// 查询最新的10个点赞
		List<ThumbsUpVO> thumbsUpList = this.thumbsUpService.list(
				QueryBuilder.create(ThumbsUpMapping.class).and(ThumbsUpMapping.BUSINESSID, id).end().sort(ThumbsUpMapping.CREATETIME).desc().enablePaging().page(1, 5).end());
		List<Long> memberIdList = new ArrayList<Long>();
		if (!CollectionUtils.isEmpty(thumbsUpList)) {
			List<Long> userIdList = thumbsUpList.stream().map(item -> item.getCreateBy()).distinct().collect(Collectors.toList());
			memberIdList.addAll(userIdList);
		}
		memberIdList.add(post.getCreateBy());
		List<SimpleMemberInfo> memberList = this.memberService.getSimpleMemberInfo(memberIdList);
		if (!CollectionUtils.isEmpty(thumbsUpList)) {
			thumbsUpList.stream().forEach(item -> {
				memberList.stream().filter(m -> m.getId().equals(item.getCreateBy())).findAny().ifPresent(m -> {
					item.setMemberInfo(m);
				});
				;
			});
		}
		memberList.stream().forEach(item -> {
			if (item.getId().equals(post.getCreateBy())) {
				post.setMemberInfo(item);
			}
		});
		post.setThumbsUpList(thumbsUpList);
		post.setMyThumbsUp(this.thumbsUpService
				.count(QueryBuilder.create(ThumbsUpMapping.class).and(ThumbsUpMapping.BUSINESSID, id).and(ThumbsUpMapping.CREATEBY, getCurrentUserId()).end())>0);
		// 填充图片
		if (!CollectionUtils.isEmpty(post.getAttachList())) {
			List<Long> attachIdList = post.getAttachList().stream().map(item -> item.getId()).distinct().collect(Collectors.toList());
			List<AttachVO> attachList = AttachRWHandle.sign(attachIdList);
			post.getAttachList().stream().forEach(item -> {
				attachList.stream().filter(a -> a.getId().equals(item.getId())).findAny().ifPresent(a -> {
					BeanUtils.copyProperties(a, item);
				});
			});
		}

		return post;
	}

	@Override
	public Boolean thumbsUp(Long id) {
		PostVO post = this.get(id);
		if(post == null) {
			throw new SystemException("动态已经被删除",CoreExceptionMessage.NOTFOUND_ERR);
		}
		List<ThumbsUpVO> list = thumbsUpService
				.list(QueryBuilder.create(ThumbsUpMapping.class).and(ThumbsUpMapping.CREATEBY, SecurityContextHelper.getCurrentUserId()).and(ThumbsUpMapping.BUSINESSID, id).end());
		if (CollectionUtils.isEmpty(list)) {
			ThumbsUpVO vo = new ThumbsUpVO();
			vo.setBusinessId(id);
			vo.setBusinessType(Integer.parseInt(CollectType.post.getValue()));
//			if(post.getCreateBy().equals(getCurrentUserId())) {
//				vo.setStatus(1);
//				vo.setStatusTs(new Timestamp(System.currentTimeMillis()));
//			}else {
//				vo.setStatus(0);
//				vo.setStatusTs(new Timestamp(System.currentTimeMillis()));
//			}
			thumbsUpService.create(vo);
			SpringApplicationContext.publishEvent(new ThumbsUpCreateEvent(vo));
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Boolean cancelThumbsUp(Long id) {
		PostVO post = this.get(id);
		if(post == null) {
			throw new SystemException("动态已经被删除",CoreExceptionMessage.NOTFOUND_ERR);
		}
		List<ThumbsUpVO> list = thumbsUpService
				.list(QueryBuilder.create(ThumbsUpMapping.class).and(ThumbsUpMapping.CREATEBY, SecurityContextHelper.getCurrentUserId()).and(ThumbsUpMapping.BUSINESSID, id).end());
		if (!CollectionUtils.isEmpty(list)) {
			thumbsUpService.remove(list.stream().map(item -> item.getId()).collect(Collectors.toList()));
			SpringApplicationContext.publishEvent(new ThumbsUpDeleteEvent(list.get(0)));
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Boolean collect(Long id) {
		PostVO post = this.get(id);
		if(post == null) {
			throw new SystemException("动态已经被删除",CoreExceptionMessage.NOTFOUND_ERR);
		}
		List<CollectVO> list = collectService
				.list(QueryBuilder.create(ThumbsUpMapping.class).and(ThumbsUpMapping.CREATEBY, SecurityContextHelper.getCurrentUserId()).and(ThumbsUpMapping.BUSINESSID, id).end());
		if (CollectionUtils.isEmpty(list)) {
			CollectVO vo = new CollectVO();
			vo.setBusinessId(id);
			vo.setBusinessType(Integer.parseInt(CollectType.post.getValue()));
			collectService.create(vo);
			SpringApplicationContext.publishEvent(new CollectCreateEvent(vo));
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Boolean cancelCollect(Long id) {
		PostVO post = this.get(id);
		if(post == null) {
			throw new SystemException("动态已经被删除",CoreExceptionMessage.NOTFOUND_ERR);
		}
		List<CollectVO> list = collectService
				.list(QueryBuilder.create(ThumbsUpMapping.class).and(ThumbsUpMapping.CREATEBY, SecurityContextHelper.getCurrentUserId()).and(ThumbsUpMapping.BUSINESSID, id).end());
		if (!CollectionUtils.isEmpty(list)) {
			collectService.remove(list.stream().map(item -> item.getId()).collect(Collectors.toList()));
			SpringApplicationContext.publishEvent(new CollectDeleteEvent(list.get(0)));
			return true;
		} else {
			return false;
		}
	}

	// private List<TopicVO> getAllTopicList(){
	// List<TopicVO> topicList = new ArrayList<TopicVO>();
	//// 0 系统推荐 1 研究领域 2 行业 3 社团 4 个性 5 状态标签 6 学校 7 专业
	// List<TagVO> tagList =
	// this.tagService.list(QueryBuilder.create(TagMapping.class).sort(TagMapping.SN).desc());
	// tagList.stream().forEach(item ->{
	// TopicVO topic = null;
	// if(TagCategory.systemTopic.getValue().equals(item.getCategory())) {
	// topic = new
	// TopicVO(item.getId(),BBSTopicType.system,item.getName(),item.getIcon(),item.getSn());
	// }
	// if(TagCategory.personality.getValue().equals(item.getCategory())) {
	// topic = new
	// TopicVO(item.getId(),BBSTopicType.personalityTag,item.getName(),item.getIcon(),item.getSn());
	// }
	// if(TagCategory.statement.getValue().equals(item.getCategory())) {
	// topic = new
	// TopicVO(item.getId(),BBSTopicType.statementTag,item.getName(),item.getIcon(),item.getSn());
	// }
	// if(topic != null) {
	// topicList.add(topic);
	// }
	// });
	// List<SchoolVO> schoolList =
	// this.schoolService.list(QueryBuilder.create(SchoolMapping.class).sort(SchoolMapping.SN).desc());
	// schoolList.stream().forEach(item ->{
	// TopicVO topic = new
	// TopicVO(item.getId(),BBSTopicType.school,item.getName(),item.getSn());
	// topicList.add(topic);
	// });
	//
	// List<SpecialismVO> specialismList =
	// this.specialismService.list(QueryBuilder.create(SpecialismMapping.class).sort(SpecialismMapping.SN).desc());
	// specialismList.stream().forEach(item ->{
	// TopicVO topic = new
	// TopicVO(item.getId(),BBSTopicType.specialism,item.getName(),item.getSn());
	// topicList.add(topic);
	// });
	//
	// List<ResearchDomainVO> researchList =
	// this.researchDomainService.list(QueryBuilder.create(ResearchDomainMapping.class).sort(ResearchDomainMapping.SN).desc());
	// researchList.stream().forEach(item ->{
	// TopicVO topic = new
	// TopicVO(item.getId(),BBSTopicType.researchDomain,item.getName(),item.getSn());
	// topicList.add(topic);
	// });
	// List<DictDataVO> industryList = dictService.findAllFromCache("experience");
	// industryList.stream().forEach(item ->{
	// TopicVO topic = new
	// TopicVO(item.getId(),BBSTopicType.researchDomain,item.getDictShowName(),item.getSn().intValue());
	// topicList.add(topic);
	// });
	// List<DictDataVO> experienceList = dictService.findAllFromCache("experience");
	// experienceList.stream().forEach(item ->{
	// TopicVO topic = new
	// TopicVO(item.getId(),BBSTopicType.researchDomain,item.getDictShowName(),item.getSn().intValue());
	// topicList.add(topic);
	// });
	// return topicList;
	// }

	@Autowired
	private TagService tagService;

	@Override
	public List<TopicGroupVO> getTopicList() {
		List<TopicGroupVO> list = new ArrayList<TopicGroupVO>();
		// type目前支持 0 系统推荐 1 研究领域 2 行业 3 社团 4 个性 5 状态标签 6 学校 7 专业
		WholeMemberDTO member = this.memberService.getWholeMemberInfo(getCurrentUserId());
		if (!CollectionUtils.isEmpty(member.getEducationList())) {
			TopicGroupVO schoolGroup = new TopicGroupVO(BBSTopicType.school);
			TopicGroupVO specialismGroup = new TopicGroupVO(BBSTopicType.specialism);
			// 构建学校，专业数据
			List<TopicVO> schoolTopicList = new ArrayList<TopicVO>();
			List<TopicVO> specialismTopicList = new ArrayList<TopicVO>();
			member.getEducationList().forEach(item -> {
				TopicVO school = new TopicVO(item.getSchool().getId(), BBSTopicType.school, item.getSchool().getAbbreviation(), item.getSchool().getSn());
				schoolTopicList.add(school);
				TopicVO specialism = new TopicVO(item.getSpecialism().getId(), BBSTopicType.specialism, item.getSpecialism().getName(), item.getSpecialism().getSn());
				specialismTopicList.add(specialism);
			});
			
			schoolTopicList.sort((a, b) -> a.getSn().compareTo(b.getSn()));
			specialismTopicList.sort((a, b) -> a.getSn().compareTo(b.getSn()));
			schoolGroup.setTopicList(schoolTopicList.stream().filter(StreamUtils.distinctByKey(item -> item.getId())).collect(Collectors.toList()));
			specialismGroup.setTopicList(specialismTopicList.stream().filter(StreamUtils.distinctByKey(item -> item.getName())).collect(Collectors.toList()));
			list.add(schoolGroup);
			list.add(specialismGroup);
		}
		// 构建研究领域
		if (!CollectionUtils.isEmpty(member.getResearchList())) {
			TopicGroupVO group = new TopicGroupVO(BBSTopicType.researchDomain);
			List<TopicVO> topicList = new ArrayList<TopicVO>();
			member.getResearchList().forEach(item -> {
				TopicVO topic = new TopicVO(item.getDomain().getId(), BBSTopicType.researchDomain, item.getDomain().getName(), item.getDomain().getSn());
				topicList.add(topic);
			});
			topicList.sort((a, b) -> a.getSn().compareTo(b.getSn()));
			group.setTopicList(topicList.stream().filter(StreamUtils.distinctByKey(item -> item.getName())).collect(Collectors.toList()));
			list.add(group);
		}

		// 构建行业
		if (!CollectionUtils.isEmpty(member.getInternshipList())) {
			TopicGroupVO group = new TopicGroupVO(BBSTopicType.industry);
			List<TopicVO> topicList = new ArrayList<TopicVO>();
			List<String> categoryList = member.getInternshipList().stream().map(item -> item.getIndustry().getCategory()).distinct().collect(Collectors.toList());
			List<DictDataVO> industryList = dictService.findAllFromCache("industry");
			industryList.stream().forEach(item -> {
				if (categoryList.contains(item.getDictValue())) {
					TopicVO topic = new TopicVO(Long.parseLong(item.getDictValue()), BBSTopicType.industry, item.getDictShowName(), item.getSn().intValue());
					topicList.add(topic);
				}
			});
			topicList.sort((a, b) -> a.getSn().compareTo(b.getSn()));
			group.setTopicList(topicList.stream().filter(StreamUtils.distinctByKey(item -> item.getName())).collect(Collectors.toList()));
			list.add(group);
		}
		// 构建社团
		if (!CollectionUtils.isEmpty(member.getProjectList())) {
			TopicGroupVO group = new TopicGroupVO(BBSTopicType.projectType);
			List<TopicVO> topicList = new ArrayList<TopicVO>();
			List<String> categoryList = member.getProjectList().stream().map(item -> item.getCategory()).distinct().collect(Collectors.toList());
			List<DictDataVO> industryList = dictService.findAllFromCache("experience");
			industryList.stream().forEach(item -> {
				if (categoryList.contains(item.getDictValue())) {
					TopicVO topic = new TopicVO(Long.parseLong(item.getDictValue()), BBSTopicType.projectType, item.getDictShowName(), item.getSn().intValue());
					topicList.add(topic);
				}
			});
			topicList.sort((a, b) -> a.getSn().compareTo(b.getSn()));
			group.setTopicList(topicList.stream().filter(StreamUtils.distinctByKey(item -> item.getName())).collect(Collectors.toList()));
			list.add(group);
		}

		// 构建个性标签
		if (!CollectionUtils.isEmpty(member.getTagsList())) {
			TopicGroupVO group = new TopicGroupVO(BBSTopicType.personalityTag);
			List<TopicVO> topicList = new ArrayList<TopicVO>();
			member.getTagsList().stream().filter(item -> item.getTag().getCategory().equals(TagCategory.personality.getValue())).forEach(item -> {
				TopicVO topic = new TopicVO(item.getTag().getId(), BBSTopicType.personalityTag, item.getTag().getName(), item.getTag().getIcon(), item.getTag().getSn());
				topicList.add(topic);
			});
			if (!CollectionUtils.isEmpty(topicList)) {
				topicList.sort((a, b) -> a.getSn().compareTo(b.getSn()));
				group.setTopicList(topicList.stream().filter(StreamUtils.distinctByKey(item -> item.getName())).collect(Collectors.toList()));
				list.add(group);
			}
		}
		// 状态标签
		if (!CollectionUtils.isEmpty(member.getTagsList())) {
			TopicGroupVO group = new TopicGroupVO(BBSTopicType.statementTag);
			List<TopicVO> topicList = new ArrayList<TopicVO>();
			member.getTagsList().stream().filter(item -> item.getTag().getCategory().equals(TagCategory.statement.getValue())).forEach(item -> {
				TopicVO topic = new TopicVO(item.getTag().getId(), BBSTopicType.statementTag, item.getTag().getName(), item.getTag().getIcon(), item.getTag().getSn());
				topicList.add(topic);
			});
			if (!CollectionUtils.isEmpty(topicList)) {
				topicList.sort((a, b) -> a.getSn().compareTo(b.getSn()));
				group.setTopicList(topicList.stream().filter(StreamUtils.distinctByKey(item -> item.getName())).collect(Collectors.toList()));
				list.add(group);
			}
		}

		List<TagVO> systemTags = tagService.list(QueryBuilder.create(TagMapping.class).and(TagMapping.CATEGORY, TagCategory.systemTopic.getValue()).end().enablePaging().page(1, 10)
				.end().sort(TagMapping.SN).asc());
		// 系统推荐
		if (!CollectionUtils.isEmpty(systemTags)) {
			TopicGroupVO group = new TopicGroupVO(BBSTopicType.systemSuggestTag);
			List<TopicVO> topicList = new ArrayList<TopicVO>();
			
			List<TopicVO> memberTopics = list.stream().flatMap(a->a.getTopicList().stream()).collect(Collectors.toList());

			systemTags.stream().filter(item -> !memberTopics.stream().filter(x->item.getName().equals(x.getName())).findAny().isPresent()).forEach(item -> {
				TopicVO topic = new TopicVO(item.getId(), BBSTopicType.systemSuggestTag, item.getName(), item.getIcon(), item.getSn());
				if(item.getRelationTo()!=null) {
					TagVO relationTag  = this.tagService.get(item.getRelationTo());
					if(relationTag.getCategory().equals(TagCategory.statement.getValue())) {
						topic.setType(BBSTopicType.statementTag.getValue());
					}else if(relationTag.getCategory().equals(TagCategory.personality.getValue())) {
						topic.setType(BBSTopicType.personalityTag.getValue());
					}
//					topic.setIcon(relationTag.getIcon());
//					topic.setName(relationTag.getName());
					topic.setId(item.getRelationTo());
				}
				topicList.add(topic);
			});

			if (!CollectionUtils.isEmpty(topicList)) {
				topicList.sort((a, b) -> a.getSn().compareTo(b.getSn()));
				group.setTopicList(topicList.stream().filter(StreamUtils.distinctByKey(item -> item.getName())).collect(Collectors.toList()));
				list.add(group);
			}
		}
		return list;
	}

	@Override
	public void onApplicationEvent(BusinessEvent<?> event) {
		if(event.isProcessed(getClass())) {
			return;
		}
		if (event instanceof CommentCreateEvent) {
			event.logDetails();
			this.onCommentCreateEvent(CommentCreateEvent.class.cast(event));
		}
		if (event instanceof CommentDeleteEvent) {
			event.logDetails();
			this.onCommentDeleteEvent(CommentDeleteEvent.class.cast(event));
		}
		if (event instanceof ThumbsUpCreateEvent) {
			event.logDetails();
			this.onThumbsUpCreateEvent(ThumbsUpCreateEvent.class.cast(event));
		}
		if (event instanceof ThumbsUpDeleteEvent) {
			event.logDetails();
			this.onThumbsUpDeleteEvent(ThumbsUpDeleteEvent.class.cast(event));
		}
	}

	private void onThumbsUpDeleteEvent(ThumbsUpDeleteEvent event) {
		this.repository.updateThumbsUpCount(event.getEventData().getBusinessId());
	}

	private void onThumbsUpCreateEvent(ThumbsUpCreateEvent event) {
		this.repository.updateThumbsUpCount(event.getEventData().getBusinessId());
	}

	private void onCommentDeleteEvent(CommentDeleteEvent event) {
		this.repository.updateCommentCount(event.getEventData().getPostId());
	}

	private void onCommentCreateEvent(CommentCreateEvent event) {
		this.repository.updateCommentCount(event.getEventData().getPostId());
	}
	@Deprecated
	@Override
	public Page<BBSNotificationVO> getNotification(Integer autoRead, Integer pageNo, Integer pageSize) {
		Page<BBSNotificationVO> page = new Page<>();
		page.setTotalCount(repository.getNotificationCount(getCurrentUserId()));
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		if (page.getTotalCount() > 0 && (page.getTotalCount() / pageSize + 1) >= pageNo) {
			List<BBSNotificationPO> list = repository.getNotificationList(getCurrentUserId(), Math.max(pageNo - 1, 0) * pageSize, pageSize);
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
			if (autoRead != null && autoRead == 1) {
				//标记为已读
//				 this.notificationService.markAsRead(getCurrentUserId(), Arrays.asList(NotificationType.comment.getValue(),NotificationType.replyComment.getValue(),NotificationType.thumbsUp.getValue()));
				 this.notificationService.markAsRead(voList.stream().map(item -> item.getId()).collect(Collectors.toList()));
			}

			page.setData(voList);
		}
		return page;
	}

	@Override
	public Page<ThumbsUpVO> getPostThumbsUps(Long id, Integer pageNo, Integer pageSize) {
		// 查询最新的10个点赞
		Page<ThumbsUpVO> page = this.thumbsUpService.page(QueryBuilder.create(ThumbsUpMapping.class).and(ThumbsUpMapping.BUSINESSID, id).end()
				.sort(ThumbsUpMapping.CREATETIME).desc().enablePaging().page(pageNo, pageSize).end());
		if (!CollectionUtils.isEmpty(page.getData())) {
			List<Long> userIdList = page.getData().stream().map(item -> item.getCreateBy()).distinct().collect(Collectors.toList());
			List<SimpleMemberInfo> memberList = this.memberService.getSimpleMemberInfo(userIdList);
			page.getData().stream().forEach(item -> {
				memberList.stream().filter(m -> m.getId().equals(item.getCreateBy())).findAny().ifPresent(m -> {
					item.setMemberInfo(m);
				});
				;
			});
		}
		return page;
	}

	@Override
	public Boolean removeById(Long pk) {
		PostVO vo = this.getSampleInfo(pk);
		if (vo != null) {
			if (!vo.getCreateBy().equals(SecurityContextHelper.getCurrentUserId())) {
				throw new SystemException(CoreExceptionMessage.NO_PERMISSION);
			}
		}else {
			return true;
		}
		this.remove(pk);
		BusinessEvent<?> event = new PostDeleteEvent(vo);
		memberTagsService.onPostChange(event);
		SpringApplicationContext.publishEvent(event);
		this.commentService.removeByPostId(pk);
		this.collectService.removeByPostId(pk);
		this.thumbsUpService.removeByPostId(pk);
		this.notificationService.removeByPostId(pk);
		
		return true;
	}

}
