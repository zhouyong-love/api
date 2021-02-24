package com.cloudok.uc.mapper;

import java.util.List;

import com.cloudok.core.mapper.IMapper;
import com.cloudok.uc.po.MessageThreadMembersPO;
import com.cloudok.uc.vo.MessageThreadMembersVO;

public interface MessageThreadMembersMapper extends IMapper<MessageThreadMembersPO>{

	void batchRead(List<MessageThreadMembersVO> readList);

}
