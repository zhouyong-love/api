package com.cloudok.base.message.service;

import java.util.List;

import com.cloudok.base.message.po.MessagePO;
import com.cloudok.base.message.service.impl.MessageServiceImpl.MessageType;
import com.cloudok.base.message.vo.Message;
import com.cloudok.base.message.vo.MessageVO;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.service.IService;
import com.cloudok.core.vo.Page;

public interface MessageService extends IService<MessageVO, MessagePO> {

	List<MessageVO> createMessage(Message message);

	List<MessageType> messageTypes();

	Page<MessageVO> iipage(QueryBuilder builder);

	void read(List<Long> ids);

}
