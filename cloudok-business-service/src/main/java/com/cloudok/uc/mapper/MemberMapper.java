package com.cloudok.uc.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cloudok.core.mapper.IMapper;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.uc.po.LinkMemberPO;
import com.cloudok.uc.po.MemberCirclePO;
import com.cloudok.uc.po.MemberPO;
import com.cloudok.uc.po.SuggsetMemberScorePO;

public interface MemberMapper extends IMapper<MemberPO>{

//	List<LinkMemberPO> queryLinkMember(QueryBuilder builder);
//	
//	Long countQueryLinkMember(QueryBuilder builder);
	
	List<LinkMemberPO> queryFriends(QueryBuilder builder);
	
//	Long friendCount(QueryBuilder builder);
	
//	List<LinkMemberPO> friend(QueryBuilder builder);

	/**
	 * 推荐
	 * @param excludedIdList 排除哪些
	 * @param filterType
	 * @param ri 
	 * @param limit 最大数量
	 * @return
	 */
	List<SuggsetMemberScorePO> suggest(@Param("excludedIdList")List<Long> excludedIdList,@Param("filterType") Integer filterType,@Param("limit") Integer limit);

	Long getMemberCirclesCount(@Param("excludedIdList")List<Long> excludedIdList,@Param("filterType") Integer type,@Param("businessId") Long businessId);
	
	List<MemberCirclePO> getMemberCirclesList(@Param("excludedIdList")List<Long> excludedIdList,@Param("filterType") Integer type,@Param("businessId") Long businessId,
			@Param("offset") Integer offset,
			@Param("pageSize") Integer pageSize);
	
}
