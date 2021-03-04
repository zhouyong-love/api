package com.cloudok.bbs.mapper;

import com.cloudok.core.mapper.IMapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cloudok.bbs.po.PostPO;

public interface PostMapper extends IMapper<PostPO>{

	public Long getMyCollectPostsCount( @Param("memberId") Long memberId);

	public List<PostPO> getMyCollectPosts( @Param("memberId") Long memberId, @Param("start") Integer start, @Param("end") Integer end);

	public void updateByMember(PostPO po);

	public Long getPeersCount(@Param("topicId") Long topicId, @Param("topicType") Integer topicType);

	public void updateThumbsUpCount(@Param("postId")Long postId);

	public void updateCommentCount(@Param("postId")Long postId);
	
	
}
