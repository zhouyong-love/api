package com.cloudok.uc.service;

import com.cloudok.core.service.IService;
import com.cloudok.uc.po.MessagePO;
import com.cloudok.uc.vo.MessageVO;

public interface MessageService extends IService<MessageVO,MessagePO>{

	Integer removeByMember(Long id);

	void deleteByThreadId(Long threadId);

}
