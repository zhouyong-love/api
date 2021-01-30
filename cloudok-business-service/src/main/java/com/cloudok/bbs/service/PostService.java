package com.cloudok.bbs.service;

import java.util.List;

import com.cloudok.bbs.po.PostPO;
import com.cloudok.bbs.vo.PostVO;
import com.cloudok.core.service.IService;
import com.cloudok.core.vo.Page;

public interface PostService extends IService<PostVO,PostPO>{

	Page<PostVO> searchByTopic(List<Long> topicIdList, Integer pageNo, Integer pageSize);

	public void customUpdate(PostVO vo);

	Boolean cancelThumbsUp(Long id);

	Boolean thumbsUp(Long id);

	Boolean collect(Long id);

	Boolean cancelCollect(Long id);

	Page<PostVO> getMyCollectPosts(Long currentUserId, Integer pageNo, Integer pageSize);

}
