package com.cloudok.uc.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cloudok.core.mapper.IMapper;
import com.cloudok.uc.po.MemberCirclePO;
import com.cloudok.uc.po.MemberPO;
import com.cloudok.uc.po.MemberSuggestScore;
import com.cloudok.uc.po.SuggsetMemberScorePO;

public interface MemberMapper extends IMapper<MemberPO>{

//	List<LinkMemberPO> queryLinkMember(QueryBuilder builder);
//	
//	Long countQueryLinkMember(QueryBuilder builder);
	
//	List<LinkMemberPO> queryFriends(QueryBuilder builder);
	
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
	@Deprecated
	List<SuggsetMemberScorePO> suggest(@Param("excludedIdList")List<Long> excludedIdList,@Param("filterType") Integer filterType,@Param("limit") Integer limit);

	Long getMemberCirclesCount(
			@Param("currentUserId") Long currentUserId,
			@Param("excludedIdList")List<Long> excludedIdList,
			@Param("filterType") Integer filterType,
			@Param("type") Integer type,
			@Param("businessId") Long businessId);
	
	List<MemberCirclePO> getMemberCirclesList(
			@Param("currentUserId") Long currentUserId,
			@Param("excludedIdList")List<Long> excludedIdList,
			@Param("filterType") Integer filterType,
			@Param("type") Integer type,
			@Param("businessId") Long businessId,
			@Param("offset") Integer offset,
			@Param("pageSize") Integer pageSize);
	
	
	 void markAsSuggested(@Param("ownerId") Long ownerId,@Param("targetIdList") List<Long> targetIdList);
	 
	 void createScoreList(@Param("list") List<MemberSuggestScore> list);
	 
	 void updateScore(MemberSuggestScore score);
	 
	 List<MemberSuggestScore> getScoreByOwnerId(@Param("idList") List<Long> idList);
	 
	 List<MemberSuggestScore> getScoreByOwnerIdAndTargetId(@Param("currentUserId") Long currentUserId,@Param("idList") List<Long> idList);

	 @Deprecated
	 List<MemberSuggestScore> getLastestSuggest(@Param("currentUserId") Long currentUserId,@Param("date")  String date, @Param("size") int size);

	/**
	 * @param ignoreSuggestStatus 是否忽略已经推荐了的
	 * @param ignoreRecognized 是否忽略已经认可的
	 * @param excludedIdList 二次回退的时候，要排除今天已经关注的
	 * @param currentUserId
	 * @param filterType  0不过滤 1 按专业 2 按行业 3 按同好
	 * @param fallbackType  null 不回退  1 回退到大类 2 直接不过滤 对于tag，1，2都是不过滤
	 * @param size
	 * @return
	 */
	List<MemberSuggestScore> suggestNew(
			@Param("ignoreSuggestStatus")boolean ignoreSuggestStatus,
			@Param("ignoreRecognized") boolean ignoreRecognized,
			@Param("excludedIdList")List<Long> excludedIdList,
			@Param("currentUserId") Long currentUserId,
			@Param("filterType") Integer filterType,
			@Param("fallbackType") Integer fallbackType, 
			@Param("size") int size);

	void resetSuggestStatus(@Param("currentUserId") Long currentUserId);
	
	List<MemberSuggestScore> getUnSuggestList(@Param("currentUserId") Long currentUserId);
	
	Long getUnSuggestCount(@Param("currentUserId") Long currentUserId);

	Long getMemberCirclesCountV2(
			@Param("currentUserId") Long currentUserId,
			@Param("excludedIdList")List<Long> excludedIdList,
			@Param("type") Integer type,
			@Param("businessId") Long businessId);
	
	List<MemberCirclePO> getMemberCirclesListV2(
			@Param("currentUserId") Long currentUserId,
			@Param("excludedIdList")List<Long> excludedIdList,
			@Param("type") Integer type,
			@Param("businessId") Long businessId,
			@Param("offset") Integer offset,
			@Param("pageSize") Integer pageSize);

	void updateKAB(@Param("currentUserId") Long currentUserId, @Param("memberIdList") List<Long> suggestMemberIdList);

	Long getSuggestV3Count(@Param("currentUserId") Long currentUserId);
	
	List<MemberSuggestScore> getSuggestV3List(
			@Param("currentUserId") Long currentUserId,
			@Param("offset") Integer offset,
			@Param("pageSize") Integer pageSize);

	Long getShouldFxixedRecognizedSize();
	
	Long getSuggestTotal();

	void resetOtherMemberOpenId(@Param("openId")String openid);

	void bindOpenId(@Param("currentUserId")Long currentUserId,@Param("openId") String openid);

	void unbindOpenId(@Param("currentUserId")Long currentUserId);
 
}
