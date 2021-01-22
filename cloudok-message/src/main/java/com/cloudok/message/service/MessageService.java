package com.cloudok.message.service;

import java.util.List;

import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.service.IService;
import com.cloudok.core.vo.Page;
import com.cloudok.message.po.MessagePO;
import com.cloudok.message.service.impl.MessageServiceImpl.MessageType;
import com.cloudok.message.vo.Message;
import com.cloudok.message.vo.MessageVO;

public interface MessageService extends IService<MessageVO, MessagePO> {

	List<MessageVO> createMessage(Message message);

	List<MessageType> messageTypes();

	Page<MessageVO> iipage(QueryBuilder builder);

	void read(List<Long> ids);

}
