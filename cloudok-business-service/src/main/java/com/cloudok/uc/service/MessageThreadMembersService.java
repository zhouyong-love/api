package com.cloudok.uc.service;

import java.util.List;

import com.cloudok.core.service.IService;
import com.cloudok.uc.po.MessageThreadMembersPO;
import com.cloudok.uc.vo.MessageThreadMembersVO;

public interface MessageThreadMembersService extends IService<MessageThreadMembersVO,MessageThreadMembersPO>{

	void batchRead(List<MessageThreadMembersVO> readList);

}
