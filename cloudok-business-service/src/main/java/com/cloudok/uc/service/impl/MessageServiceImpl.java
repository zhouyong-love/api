package com.cloudok.uc.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cloudok.core.exception.CoreExceptionMessage;
import com.cloudok.core.exception.SystemException;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.service.AbstractService;
import com.cloudok.enums.UCMessageThreadType;
import com.cloudok.security.SecurityContextHelper;
import com.cloudok.uc.mapper.MessageMapper;
import com.cloudok.uc.mapping.MessageMapping;
import com.cloudok.uc.mapping.MessageThreadMembersMapping;
import com.cloudok.uc.po.MessagePO;
import com.cloudok.uc.service.MessageService;
import com.cloudok.uc.service.MessageThreadMembersService;
import com.cloudok.uc.service.MessageThreadService;
import com.cloudok.uc.vo.MessageThreadMembersVO;
import com.cloudok.uc.vo.MessageThreadVO;
import com.cloudok.uc.vo.MessageVO;

@Service("UCMessageServiceImpl")
public class MessageServiceImpl extends AbstractService<MessageVO, MessagePO> implements MessageService {

	@Autowired
	public MessageServiceImpl(MessageMapper repository) {
		super(repository);
	}

//	@Autowired
//	private MessageMapper repository;

	@Autowired
	private MessageThreadService messageThreadService;

	@Autowired
	private MessageThreadMembersService messageThreadMembersService;

	@Override
	public Integer removeByMember(Long id) {
		MessageVO vo = this.get(id);
		if (vo != null) {
			if (!vo.getMemberId().equals(SecurityContextHelper.getCurrentUserId())) {
				throw new SystemException("只能删除自己发送的消息", CoreExceptionMessage.NO_PERMISSION);
			}
		}
		return this.remove(id);
	}

	@Override
	public void deleteByThreadId(Long threadId) {
		MessageThreadVO messateThread = messageThreadService.get(threadId);
		if (UCMessageThreadType.chat.getValue().equals(messateThread.getType())) {
			throw new SystemException("暂时不支持删除私信", CoreExceptionMessage.NO_PERMISSION);
		}
		List<MessageThreadMembersVO> members = messageThreadMembersService.list(
				QueryBuilder.create(MessageThreadMembersMapping.class).and(MessageMapping.THREADID, threadId).end());
		List<MessageVO> messages = this
				.list(QueryBuilder.create(MessageMapping.class).and(MessageMapping.THREADID, threadId).end());
		if (CollectionUtils.isEmpty(messages)) {
			this.remove(messages.stream().map(item -> item.getId()).collect(Collectors.toList()));
		}
		if (CollectionUtils.isEmpty(members)) {
			messageThreadMembersService.remove(members.stream().map(item -> item.getId()).collect(Collectors.toList()));
		}
		messageThreadService.remove(threadId);
	}
}
