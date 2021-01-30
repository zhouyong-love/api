package com.cloudok.bbs.mapper;

import com.cloudok.core.mapper.IMapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cloudok.bbs.po.PostPO;

public interface PostMapper extends IMapper<PostPO>{

	public void customUpdate(PostPO po);

	public Long searchByTopicCount(@Param("topicIdList") List<Long> topicIdList);

	public List<PostPO> searchByTopic(@Param("topicIdList") List<Long> topicIdList, @Param("start") Integer start, @Param("end") Integer end);

	public Long getMyCollectPostsCount( @Param("memberId") Long memberId);

	public List<PostPO> getMyCollectPosts( @Param("memberId") Long memberId, @Param("start") Integer start, @Param("end") Integer end);
	
	
}
