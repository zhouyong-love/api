package com.cloudok.bbs.vo;

import java.util.List;

import com.cloudok.base.attach.vo.AttachVO;
import com.cloudok.core.vo.VO;
import com.cloudok.uc.dto.SimpleMemberInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostVO extends VO {

	private static final long serialVersionUID = 135668491124968370L;

	private String content;

	private Integer thumbsUpCount;

	private Integer replyCount;

	private Integer collectCount;

	private List<TopicVO> topicList;

	private List<AttachVO> attachList;

	private List<CommentVO> comments;
	
	private Boolean isPublic;
	
	private SimpleMemberInfo member;
	
	
	
	

}
