package com.cloudok.bbs.mapper;

import org.apache.ibatis.annotations.Param;

import com.cloudok.bbs.po.ThumbsUpPO;
import com.cloudok.core.mapper.IMapper;

public interface ThumbsUpMapper extends IMapper<ThumbsUpPO> {
//	
//	@Deprecated
//	void markAsRead(@Param("idList") List<Long> idList);

	void removeByPostId(@Param("postId") Long postId);

}
