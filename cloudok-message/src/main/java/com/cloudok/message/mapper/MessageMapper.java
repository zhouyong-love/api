package com.cloudok.message.mapper;

import java.util.List;

import com.cloudok.core.mapper.IMapper;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.message.po.MessagePO;
import com.cloudok.message.po.ReadMessage;

public interface MessageMapper extends IMapper<MessagePO> {

	List<MessagePO> iilist(QueryBuilder builder);

	Long iicount(QueryBuilder builder);

	void readDetail(ReadMessage readMessage);

	void read(ReadMessage readMessage);

}
