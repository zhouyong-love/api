package com.cloudok.message.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudok.core.service.AbstractService;
import com.cloudok.message.mapper.MessageDetailsMapper;
import com.cloudok.message.po.MessageDetailsPO;
import com.cloudok.message.service.MessageDetailsService;
import com.cloudok.message.vo.MessageDetailsVO;

@Service
public class MessageDetailsServiceImpl extends AbstractService<MessageDetailsVO, MessageDetailsPO> implements MessageDetailsService{

	@Autowired
	public MessageDetailsServiceImpl(MessageDetailsMapper repository) {
		super(repository);
	}
}
