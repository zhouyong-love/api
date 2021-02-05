package com.cloudok.uc.service;

import javax.validation.Valid;

import com.cloudok.core.service.IService;
import com.cloudok.core.vo.Page;
import com.cloudok.uc.po.MessagePO;
import com.cloudok.uc.vo.MessageThreadVO;
import com.cloudok.uc.vo.MessageVO;

public interface MessageService extends IService<MessageVO,MessagePO>{

	MessageVO createByMember(@Valid MessageVO vo);
	
	MessageVO createByRecognized(@Valid MessageVO vo);

	MessageVO updateByMember(@Valid MessageVO vo);

	Integer removeByMember(Long id);

	Page<MessageThreadVO> searchInteractionMessages(Long memberId, Integer status,Integer pageNo, Integer pageSize);

	Page<MessageThreadVO> searchPrivateMessages(Long memberId, Integer pageNo, Integer pageSize);

	Page<MessageVO> getByThreadId(String id,Integer pageNo, Integer pageSize);
	
	void deleteByThreadId(String threadId);

	Page<MessageThreadVO> searchMyInteractionMessages(Integer viewType, Integer pageNo, Integer pageSize);

}
