package com.cloudok.bbs.service;

import com.cloudok.bbs.po.ThumbsUpPO;
import com.cloudok.bbs.vo.ThumbsUpVO;
import com.cloudok.core.service.IService;

public interface ThumbsUpService extends IService<ThumbsUpVO,ThumbsUpPO>{
//
//	@Deprecated
//	void markAsRead(List<Long> thumupIdList);

	void removeByPostId(Long postId);

}
