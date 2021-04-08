package com.cloudok.uc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
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
public class MessageThreadMembersServiceImpl extends AbstractService<MessageThreadMembersVO, MessageThreadMembersPO> implements MessageThreadMembersService,ApplicationListener<BusinessEvent<?>>,ApplicationRunner{

	@Autowired
	private MessageThreadMembersMapper repository;
	
	@Autowired
	public MessageThreadMembersServiceImpl(MessageThreadMembersMapper repository) {
		super(repository);
	}

	@Override
	public void onApplicationEvent(BusinessEvent<?> arg0) {
		if(arg0.isProcessed(getClass())) {
			return;
		}
		if(arg0 instanceof MessageSendEvent) {
			arg0.logDetails();
			this.onMessageSend(MessageSendEvent.class.cast(arg0).getEventData());
		}
		
	}
	
	public void fixUnReadCount() {
		List<MessageThreadMembersVO> vos = this.list(QueryBuilder.create(MessageThreadMembersMapping.class));
		vos.forEach(item->{
			MessageThreadMembersVO merge = new MessageThreadMembersVO();
			merge.setId(item.getId());
			merge.setUnRead(repository.getUnReadMessageCount(item.getMemberId(), item.getThreadId(), item.getLastPosition()));
			this.merge(merge);
		});
	}
	//更新最新读取位置
	private void onMessageSend(MessageVO eventData) {
		List<MessageThreadMembersVO> vos = this.list(QueryBuilder.create(MessageThreadMembersMapping.class).and(MessageThreadMembersMapping.THREADID, eventData.getThreadId())
				.end());
		vos.forEach(item->{
			MessageThreadMembersVO merge = new MessageThreadMembersVO();
			merge.setId(item.getId());
			if(item.getMemberId().equals( eventData.getMemberId())) {
				merge.setLastPosition(eventData.getId());
				merge.setUnRead(0);
			}else {
				merge.setUnRead(repository.getUnReadMessageCount(item.getMemberId(), item.getThreadId(), item.getLastPosition()));
			}
			this.merge(merge);
		});
			
	}

	@Override
	public void batchRead(List<MessageThreadMembersVO> readList) {
		if(!CollectionUtils.isEmpty(readList)) {
			repository.batchRead(readList);
		}
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		//fixUnReadCount();
	}
}
