package com.cloudok.bbs.mapper;

import com.cloudok.core.mapper.IMapper;

import org.apache.ibatis.annotations.Param;

import com.cloudok.bbs.po.CollectPO;

public interface CollectMapper extends IMapper<CollectPO>{

	void removeByPostId(@Param("postId") Long postId);

}
