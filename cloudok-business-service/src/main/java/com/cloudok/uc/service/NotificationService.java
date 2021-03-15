package com.cloudok.uc.service;

import java.util.List;

import com.cloudok.core.service.IService;
import com.cloudok.uc.po.NotificationPO;
import com.cloudok.uc.vo.NotificationVO;

public interface NotificationService extends IService<NotificationVO,NotificationPO>{

	void removeByPostId(Long postId);

	void markAsRead(Long memberId,List<String> businessTypeList);

	void markAsRead(List<Long> idList);
}
