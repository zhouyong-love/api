package com.cloudok.bbs.vo;

import com.cloudok.core.vo.VO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentVO extends VO {

	private static final long serialVersionUID = 107853640230485710L;
	
	
	private String content;
	
	
	private Integer thumbsUpCount;
	
	
	private Integer replyCount;
	
	
	private Integer collectCount;
	
	
	private Long postId;
	
	
	private String path;
	
	
	private Long parentId;
	
	
}
