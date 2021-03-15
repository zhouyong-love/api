package com.cloudok.bbs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudok.bbs.mapper.CollectMapper;
import com.cloudok.bbs.po.CollectPO;
import com.cloudok.bbs.service.CollectService;
import com.cloudok.bbs.service.PostService;
import com.cloudok.bbs.vo.CollectVO;
import com.cloudok.bbs.vo.PostVO;
import com.cloudok.core.service.AbstractService;
import com.cloudok.core.vo.Page;

@Service
public class CollectServiceImpl extends AbstractService<CollectVO, CollectPO> implements CollectService{

	@Autowired
	private PostService postService;
	@Autowired
	private CollectMapper repository;
	@Autowired
	public CollectServiceImpl(CollectMapper repository) {
		super(repository);
	}

	@Override
	public Page<PostVO> getMyCollectPosts(Long currentUserId, Integer pageNo, Integer pageSize) {
		return this.postService.getMyCollectPosts(currentUserId,pageNo,pageSize);
	}
	@Override
	public void removeByPostId(Long postId) {
		this.repository.removeByPostId(postId);
	}
}
