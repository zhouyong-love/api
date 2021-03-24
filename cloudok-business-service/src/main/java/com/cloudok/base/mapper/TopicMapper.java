package com.cloudok.base.mapper;

import org.apache.ibatis.annotations.Param;

import com.cloudok.base.po.TopicPO;
import com.cloudok.core.mapper.IMapper;

public interface TopicMapper extends IMapper<TopicPO>{

	void updatePeersCount(@Param("id")Long id,@Param("topicType") String topicType,@Param ("topicId") long topicId);

	void updatePostCount(@Param("id")Long id,@Param("topicType") Integer topicType,@Param ("topicId") long topicId);

	TopicPO getTopicInfo(@Param("topicType") Integer topicType,@Param ("topicId") long topicId);
}
