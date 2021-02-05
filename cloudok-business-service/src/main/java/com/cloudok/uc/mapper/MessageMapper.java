package com.cloudok.uc.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.cloudok.core.mapper.IMapper;
import com.cloudok.uc.po.MessagePO;
import com.cloudok.uc.po.UnReadCount;

@Repository("UCMessageMapper")
public interface MessageMapper extends IMapper<MessagePO> {

	Long searchInteractionMessagesCount(@Param("memberId") Long memberId, @Param("status") Integer status,
			@Param("currentMemberId") Long currentMemberId, @Param("seeOthers") Integer seeOthers
			);

	List<String> searchInteractionMessages(@Param("memberId") Long memberId, @Param("status") Integer status,
			@Param("currentMemberId") Long currentMemberId, @Param("seeOthers") Integer seeOthers,
			 @Param("start") Integer start, @Param("end") Integer end);

	Long searchPrivateMessagesCount(@Param("memberId") Long memberId);

	List<String> searchPrivateMessages(@Param("memberId") Long memberId, @Param("start") Integer start,
			@Param("end") Integer end);

	void deleteByThreadId(String threadId);

	List<UnReadCount> getUnReadMessages(@Param("memberId") Long memberId,
			@Param("threadIdList") List<String> threadIdList);

	void batchRead(@Param("memberId") Long memberId, @Param("threadId") String threadId,
			@Param("types") List<String> types);
	
	Long searchMyInteractionMessagesCount(@Param("memberId") Long memberId, @Param("viewType") Integer viewType);

	List<String> searchMyInteractionMessages(@Param("memberId") Long memberId, @Param("viewType") Integer viewType,  @Param("start") Integer start, @Param("end") Integer end);
	
}
