package com.cloudok.uc.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cloudok.core.mapper.IMapper;
import com.cloudok.uc.po.RecognizedPO;

public interface RecognizedMapper extends IMapper<RecognizedPO>{

	@Deprecated
	int getFriendCount(@Param("userId")Long userId);
	
	int getNewApplyCount(@Param("userId")Long userId);

	Integer getSecondDegreeRecognizedCount(@Param("currentUserId")Long currentUserId, @Param("memberId")Long memberId);

	List<RecognizedPO> getSecondDegreeRecognized(@Param("currentUserId")Long currentUserId, @Param("memberId")Long memberId,
			@Param("start") int start, 
			@Param("end")int end);
	
}
