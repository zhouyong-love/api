package com.cloudok.uc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cloudok.core.event.BusinessEvent;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.service.AbstractService;
import com.cloudok.uc.event.MessageSendEvent;
import com.cloudok.uc.mapper.MessageThreadMembersMapper;
import com.cloudok.uc.mapping.MessageThreadMembersMapping;
import com.cloudok.uc.po.MessageThreadMembersPO;
import com.cloudok.uc.service.MessageThreadMembersService;
import com.cloudok.uc.vo.MessageThreadMembersVO;
import com.cloudok.uc.vo.MessageVO;

@Service
public class MessageThreadMembersServiceImpl extends AbstractService<MessageThreadMembersVO, MessageThreadMembersPO> implements MessageThreadMembersService,ApplicationListener<BusinessEvent<?>>{

	@Autowired
	private MessageThreadMembersMapper repository;
	
	@Autowired
	public MessageThreadMembersServiceImpl(MessageThreadMembersMapper repository) {
		super(repository);
	}

	@Override
	public void onApplicationEvent(BusinessEvent<?> arg0) {
		if(arg0 instanceof MessageSendEvent) {
			this.onMessageSend(MessageSendEvent.class.cast(arg0).getEventData());
		}
		
	}
	//更新最新读取位置
	private void onMessageSend(MessageVO eventData) {
		MessageThreadMembersVO vo = this.get(QueryBuilder.create(MessageThreadMembersMapping.class).and(MessageThreadMembersMapping.THREADID, eventData.getThreadId())
				.and(MessageThreadMembersMapping.MEMBERID, eventData.getMemberId()).end());
		if(vo != null) {
			MessageThreadMembersVO merge = new MessageThreadMembersVO();
			merge.setId(vo.getId());
			merge.setLastPosition(eventData.getId());
			this.merge(merge);
		}
	}

	@Override
	public void batchRead(List<MessageThreadMembersVO> readList) {
		if(!CollectionUtils.isEmpty(readList)) {
			repository.batchRead(readList);
		}
	}
}
