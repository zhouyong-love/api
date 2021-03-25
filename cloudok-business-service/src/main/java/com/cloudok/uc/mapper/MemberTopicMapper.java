package com.cloudok.uc.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cloudok.core.mapper.IMapper;
import com.cloudok.uc.po.MemberTopicPO;
import com.cloudok.uc.po.UpdatePostCountPO;

public interface MemberTopicMapper extends IMapper<MemberTopicPO>{

	List<UpdatePostCountPO> getUpdatePostCount(@Param("currentUserId") Long currentUserId,@Param("topicIdList") List<Long> topicIdList);

}
