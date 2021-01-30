package com.cloudok.uc.mapper;

import org.springframework.stereotype.Repository;

import com.cloudok.core.mapper.IMapper;
import com.cloudok.uc.po.MessagePO;

@Repository("UCMessageMapper")
public interface MessageMapper extends IMapper<MessagePO>{

}
