package com.cloudok.bbs.mapper;

import com.cloudok.core.mapper.IMapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cloudok.bbs.po.ThumbsUpPO;

public interface ThumbsUpMapper extends IMapper<ThumbsUpPO>{

		void markAsRead(@Param("idList") List<Long> idList);

		void removeByPostId(@Param("postId") Long postId);

}
