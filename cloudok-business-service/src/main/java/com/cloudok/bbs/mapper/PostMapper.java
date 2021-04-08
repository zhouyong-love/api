package com.cloudok.bbs.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cloudok.bbs.po.BBSNotificationPO;
import com.cloudok.bbs.po.PostPO;
import com.cloudok.core.mapper.IMapper;

public interface PostMapper extends IMapper<PostPO>{

	public Long getMyCollectPostsCount( @Param("memberId") Long memberId);

	public List<PostPO> getMyCollectPosts( @Param("memberId") Long memberId, @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

	public void updateByMember(PostPO po);

	public Long getPeersCount(@Param("topicId") Long topicId, @Param("topicType") Integer topicType);

	public void updateThumbsUpCount(@Param("postId")Long postId);

	public void updateCommentCount(@Param("postId")Long postId);

	public Long getNotificationCount(@Param("currentUserId")Long currentUserId);

	public List<BBSNotificationPO> getNotificationList(@Param("currentUserId")Long currentUserId,@Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

	public Long getNotificationCountByType(@Param("currentUserId")Long currentUserId,@Param("type")Integer type);

	public List<BBSNotificationPO> getNotificationListByType(@Param("currentUserId")Long currentUserId,@Param("type")Integer type,@Param("offset") Integer offset, @Param("pageSize") Integer pageSize);
	
	Long discoverCount(@Param("currentUserId")Long currentUserId);
	
	List<PostPO> discover(@Param("currentUserId")Long currentUserId,@Param("offset") Integer offset, @Param("pageSize") Integer pageSize);
}
