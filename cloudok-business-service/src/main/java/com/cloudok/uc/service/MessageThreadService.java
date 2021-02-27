package com.cloudok.uc.service;

import javax.validation.Valid;

import com.cloudok.core.service.IService;
import com.cloudok.core.vo.Page;
import com.cloudok.uc.po.MessageThreadPO;
import com.cloudok.uc.vo.MessageThreadGroup;
import com.cloudok.uc.vo.MessageThreadVO;
import com.cloudok.uc.vo.MessageVO;

public interface MessageThreadService extends IService<MessageThreadVO,MessageThreadPO>{

	MessageThreadVO createByMember(@Valid MessageVO vo);
	
	Page<MessageVO> getMessageByThreadId(Long threadId, Integer pageNo, Integer pageSize);
	
	Page<MessageThreadVO> searchChatMessageThreads(Long memberId,Integer read, Integer pageNo, Integer pageSize);
	
	Page<MessageThreadVO> searchInteractionMessageThreads(Long memberId, Integer status,Integer pageNo, Integer pageSize);

	Page<MessageThreadVO> searchMyInteractionMessageThreads(Integer read,Integer viewType, Integer pageNo, Integer pageSize);

	MessageThreadVO getMessageThreadByMemberId(Long currentUserId,Integer read, Long memberId, Integer latestMessageCount);

	void readed(Long messageId);

	Integer getLatestMessageCount(Long currentUserId);

	MessageThreadGroup searchMyInteractionMessageThreadsGroup(Integer viewType);

}
