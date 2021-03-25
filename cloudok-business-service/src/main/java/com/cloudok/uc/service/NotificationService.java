package com.cloudok.uc.service;

import java.util.List;

import com.cloudok.bbs.vo.BBSNotificationVO;
import com.cloudok.core.service.IService;
import com.cloudok.core.vo.Page;
import com.cloudok.uc.po.NotificationPO;
import com.cloudok.uc.vo.NotificationTotalVO;
import com.cloudok.uc.vo.NotificationVO;

public interface NotificationService extends IService<NotificationVO,NotificationPO>{

	void removeByPostId(Long postId);

//	void markAsRead(Long memberId,List<String> businessTypeList);

	void markAsRead(List<Long> idList);

	List<NotificationTotalVO> getTotal();

	Page<BBSNotificationVO> getNotificationList(Integer type, Integer pageNo, Integer pageSize);
}
