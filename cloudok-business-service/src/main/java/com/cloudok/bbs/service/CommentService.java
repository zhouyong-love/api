package com.cloudok.bbs.service;

import java.util.List;

import com.cloudok.bbs.po.CommentPO;
import com.cloudok.bbs.vo.CommentVO;
import com.cloudok.core.service.IService;
import com.cloudok.core.vo.Page;

public interface CommentService extends IService<CommentVO,CommentPO>{

	Page<CommentVO> getCommentList(Long postId,Integer pageNo, Integer pageSize);

	List<CommentVO> getMyRecognizedComments(Long currentUserId, List<Long> postIdList, int maxSize);

//	@Deprecated
//	void markAsRead(List<Long> commentIdList);

	void removeByPostId(Long postId);
	
}
