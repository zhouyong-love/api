package com.cloudok.uc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudok.core.exception.CoreExceptionMessage;
import com.cloudok.core.exception.SystemException;
import com.cloudok.core.service.AbstractService;
import com.cloudok.security.SecurityContextHelper;
import com.cloudok.uc.mapper.MessageMapper;
import com.cloudok.uc.po.MessagePO;
import com.cloudok.uc.service.MessageService;
import com.cloudok.uc.vo.MessageVO;

@Service("UCMessageServiceImpl")
public class MessageServiceImpl extends AbstractService<MessageVO, MessagePO> implements MessageService{

	@Autowired
	public MessageServiceImpl(MessageMapper repository) {
		super(repository);
	}

	@Override
	public Integer removeByMember(Long id) {
		MessageVO vo = this.get(id);
		if (vo != null) {
			if (!vo.getMemberId().equals(SecurityContextHelper.getCurrentUserId())){
				throw new SystemException("只能删除自己发送的消息",CoreExceptionMessage.NO_PERMISSION);
			}
		}
		return this.remove(id);
	}
}
