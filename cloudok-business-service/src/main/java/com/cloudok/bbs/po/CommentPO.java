package com.cloudok.bbs.po;

import com.cloudok.core.po.PO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentPO extends PO {

	private static final long serialVersionUID = 420891551391458800L;

	
	private String content;
	
	
	private Integer thumbsUpCount;
	
	
	private Integer replyCount;
	
	
	private Integer collectCount;
	
	
	private Long postId;
	
	
	private String path;
	
	
	private Long parentId;
	
	
}
