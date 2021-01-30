package com.cloudok.uc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudok.core.service.AbstractService;
import com.cloudok.uc.mapper.MessageMapper;
import com.cloudok.uc.po.MessagePO;
import com.cloudok.uc.service.MessageService;
import com.cloudok.uc.vo.MessageVO;

@Service
public class MessageServiceImpl extends AbstractService<MessageVO, MessagePO> implements MessageService{

	@Autowired
	public MessageServiceImpl(MessageMapper repository) {
		super(repository);
	}
}
