package com.cloudok.bbs.service;

import java.util.List;

import javax.validation.Valid;

import com.cloudok.bbs.po.PostPO;
import com.cloudok.bbs.vo.BBSNotificationVO;
import com.cloudok.bbs.vo.PostVO;
import com.cloudok.bbs.vo.ThumbsUpVO;
import com.cloudok.bbs.vo.TopicGroupVO;
import com.cloudok.bbs.vo.TopticPostResult;
import com.cloudok.core.service.IService;
import com.cloudok.core.vo.Page;

public interface PostService extends IService<PostVO,PostPO>{

	Boolean cancelThumbsUp(Long id);

	Boolean thumbsUp(Long id);

	Boolean collect(Long id);

	Boolean cancelCollect(Long id);

	Page<PostVO> getMyCollectPosts(Long currentUserId, Integer pageNo, Integer pageSize);

	List<TopicGroupVO> getTopicList();

	PostVO createByMember(@Valid PostVO vo);

	PostVO updateByMember(@Valid PostVO vo);

	Page<PostVO> discover(Integer pageNo, Integer pageSize,Long memberId);

	TopticPostResult getPostsByTopic(Long topicId, Integer topicType, Integer pageNo, Integer pageSize);

	PostVO getDetails(Long id);

	@Deprecated
	Page<BBSNotificationVO> getNotification(Integer autoRead, Integer pageNo, Integer pageSize);

	Page<ThumbsUpVO> getPostThumbsUps(Long id, Integer pageNo, Integer pageSize);

	Boolean removeById(Long id);

	Page<PostVO>  getPostCircles(Long topicId, Integer topicType, Integer pageNo, Integer pageSize);

	List<PostVO> getBaseInfo(List<Long> postIdList);

}
