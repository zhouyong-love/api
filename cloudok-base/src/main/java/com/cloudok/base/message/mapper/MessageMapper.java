package com.cloudok.base.message.mapper;

import java.util.List;

import com.cloudok.base.message.po.MessagePO;
import com.cloudok.base.message.po.ReadMessage;
import com.cloudok.core.mapper.IMapper;
import com.cloudok.core.query.QueryBuilder;

public interface MessageMapper extends IMapper<MessagePO> {

	List<MessagePO> iilist(QueryBuilder builder);

	Long iicount(QueryBuilder builder);

	void readDetail(ReadMessage readMessage);

	void read(ReadMessage readMessage);

}
