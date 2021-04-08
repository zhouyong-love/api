package com.cloudok.uc.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cloudok.core.mapper.IMapper;
import com.cloudok.uc.po.MessageThreadMembersPO;
import com.cloudok.uc.vo.MessageThreadMembersVO;

public interface MessageThreadMembersMapper extends IMapper<MessageThreadMembersPO>{

	void batchRead(List<MessageThreadMembersVO> readList);

	Integer getUnReadMessageCount(@Param("memberId")Long memberId,@Param("threadId")Long threadId,@Param("lastPointId")Long lastPointId);
}
