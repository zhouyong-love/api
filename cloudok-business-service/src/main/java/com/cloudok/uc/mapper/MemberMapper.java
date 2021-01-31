package com.cloudok.uc.mapper;

import java.util.List;

import com.cloudok.core.mapper.IMapper;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.uc.po.LinkMemberPO;
import com.cloudok.uc.po.MemberPO;

public interface MemberMapper extends IMapper<MemberPO>{

	List<LinkMemberPO> queryLinkMember(QueryBuilder builder);
	
	Long countQueryLinkMember(QueryBuilder builder);
	
	List<LinkMemberPO> queryFriends(QueryBuilder builder);
	
}
