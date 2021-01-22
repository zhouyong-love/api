package com.cloudok.base.message.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudok.base.message.mapper.MessageDetailsMapper;
import com.cloudok.base.message.po.MessageDetailsPO;
import com.cloudok.base.message.service.MessageDetailsService;
import com.cloudok.base.message.vo.MessageDetailsVO;
import com.cloudok.core.service.AbstractService;

@Service
public class MessageDetailsServiceImpl extends AbstractService<MessageDetailsVO, MessageDetailsPO> implements MessageDetailsService{

	@Autowired
	public MessageDetailsServiceImpl(MessageDetailsMapper repository) {
		super(repository);
	}
}
