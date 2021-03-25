package com.cloudok.uc.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cloudok.core.mapper.IMapper;
import com.cloudok.uc.po.NotificationPO;
import com.cloudok.uc.po.NotificationTotalPO;

public interface NotificationMapper extends IMapper<NotificationPO>{

	void removeByPostId(@Param("postId") Long postId);

	void deleteByBusinessId(@Param("businessId") Long businessId,@Param("businessType") String businessType);

	void markAsRead(@Param("memberId")Long memberId,@Param("businessTypeList") List<String> businessTypeList);

	void markAsReadByIdList(@Param("idList") List<Long> idList);

	List<NotificationTotalPO> getTotal(@Param("memberId") Long currentUserId);



}
