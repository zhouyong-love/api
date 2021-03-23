package com.cloudok.bbs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudok.bbs.mapper.ThumbsUpMapper;
import com.cloudok.bbs.po.ThumbsUpPO;
import com.cloudok.bbs.service.ThumbsUpService;
import com.cloudok.bbs.vo.ThumbsUpVO;
import com.cloudok.core.service.AbstractService;

@Service
public class ThumbsUpServiceImpl extends AbstractService<ThumbsUpVO, ThumbsUpPO> implements ThumbsUpService{

	@Autowired
	private ThumbsUpMapper repository;
	@Autowired
	public ThumbsUpServiceImpl(ThumbsUpMapper repository) {
		super(repository);
	}
	@Override
	public ThumbsUpVO create(ThumbsUpVO d) {
//		d.setStatus(0);
//		d.setStatusTs(new Timestamp(System.currentTimeMillis()));
		return super.create(d);
	}
//	@Deprecated
//	@Override
//	public void markAsRead(List<Long> thumupIdList) {
//		if(CollectionUtils.isEmpty(thumupIdList)) {
//			return;
//		}
//		repository.markAsRead(thumupIdList);
//		
//	}
	@Override
	public void removeByPostId(Long postId) {
		this.repository.removeByPostId(postId);
	}
}
