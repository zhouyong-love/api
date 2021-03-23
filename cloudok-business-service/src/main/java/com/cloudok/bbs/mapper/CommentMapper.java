package com.cloudok.bbs.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cloudok.bbs.po.CommentPO;
import com.cloudok.core.mapper.IMapper;

public interface CommentMapper extends IMapper<CommentPO> {

	List<CommentPO> getMyRecognizedComments(@Param("currentUserId") Long currentUserId,
			@Param("postIdList") List<Long> postIdList, @Param("maxSize") int maxSize);
//
//	@Deprecated
//	void markAsRead(@Param("idList") List<Long> idList);

	void removeByPostId(@Param("postId") Long postId);

}
