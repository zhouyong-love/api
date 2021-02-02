package com.cloudok.uc.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.cloudok.core.mapper.IMapper;
import com.cloudok.uc.po.MessagePO;

@Repository("UCMessageMapper")
public interface MessageMapper extends IMapper<MessagePO>{

	Long searchInteractionMessagesCount(@Param("memberId")Long memberId,@Param("status")Integer status);

	List<String> searchInteractionMessages(@Param("memberId") Long memberId, @Param("status")Integer status,@Param("start") Integer start, @Param("end") Integer end);

	Long searchPrivateMessagesCount(@Param("memberId")Long memberId);

	List<String> searchPrivateMessages(@Param("memberId") Long memberId, @Param("start") Integer start, @Param("end") Integer end);
	
	void deleteByThreadId(String threadId);
}
