package com.cloudok.bbs.service;

import java.util.List;

import com.cloudok.bbs.po.ThumbsUpPO;
import com.cloudok.bbs.vo.ThumbsUpVO;
import com.cloudok.core.service.IService;

public interface ThumbsUpService extends IService<ThumbsUpVO,ThumbsUpPO>{

	void markAsRead(List<Long> thumupIdList);

	void removeByPostId(Long postId);

}
