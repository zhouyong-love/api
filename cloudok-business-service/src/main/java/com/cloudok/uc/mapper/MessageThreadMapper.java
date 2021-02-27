package com.cloudok.uc.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cloudok.core.mapper.IMapper;
import com.cloudok.uc.po.MessageThreadGroupPO;
import com.cloudok.uc.po.MessageThreadPO;
import com.cloudok.uc.po.UnReadCount;

public interface MessageThreadMapper extends IMapper<MessageThreadPO>{
	
	/**
	 * 根据参与者与thread类型获取thread
	 * @param memberIdList
	 * @param type
	 * @return
	 */
	public MessageThreadPO getMessageThreadByMemebrs(@Param("memberIdList")List<Long> memberIdList,@Param("type")String type);

	public List<MessageThreadPO> searchChatMessageThreads(@Param("memberId")Long memberId, @Param("start")int start, @Param("end")int end);

	public Long searchChatMessageThreadsCount(@Param("memberId")Long memberId);

	public List<UnReadCount> getUnReadMessages(@Param("memberId")Long memberId, @Param("threadIdList")List<Long> threadIdList);

	public Long searchMyInteractionMessageThreadsCount(@Param("memberId")Long memberId, @Param("viewType")Integer viewType);

	public List<MessageThreadPO> searchMyInteractionMessageThreads(@Param("memberId")Long memberId, @Param("viewType")Integer viewType,@Param("start")int start, @Param("end")int end);

	public Long searchInteractionMessageThreadsCount(@Param("memberId")Long memberId, @Param("currentUserId")Long currentUserId,@Param("status")Integer status,
			@Param("seeOthers")Integer seeOthers);

	public List<MessageThreadPO> searchInteractionMessageThreads(@Param("memberId")Long memberId, @Param("currentUserId")Long currentUserId,@Param("status")Integer status,
			@Param("seeOthers")Integer seeOthers,@Param("start")int start, @Param("end")int end);

	public Integer getLatestMessageCount(@Param("memberId")Long currentUserId);

	public List<MessageThreadGroupPO> searchMyInteractionMessageThreadsGroup(@Param("memberId")Long memberId, @Param("viewType")Integer viewType);
}
