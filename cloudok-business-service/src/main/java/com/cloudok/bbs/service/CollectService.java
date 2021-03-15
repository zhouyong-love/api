package com.cloudok.bbs.service;

import com.cloudok.bbs.po.CollectPO;
import com.cloudok.bbs.vo.CollectVO;
import com.cloudok.bbs.vo.PostVO;
import com.cloudok.core.service.IService;
import com.cloudok.core.vo.Page;

public interface CollectService extends IService<CollectVO,CollectPO>{

	Page<PostVO> getMyCollectPosts(Long currentUserId, Integer pageNo, Integer pageSize);

	void removeByPostId(Long postId);

}
