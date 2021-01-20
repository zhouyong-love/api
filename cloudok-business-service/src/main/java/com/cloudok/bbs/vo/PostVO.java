package com.cloudok.bbs.vo;

import com.cloudok.core.vo.VO;

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
	
	
}
