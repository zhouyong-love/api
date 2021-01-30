package com.cloudok.bbs.po;

import com.cloudok.core.po.PO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostPO extends PO {

	private static final long serialVersionUID = 673717022746863000L;

	
	private String content;
	
	
	private Integer thumbsUpCount;
	
	
	private Integer replyCount;
	
	
	private Integer collectCount;
	
	private String attachIds;
	
	private Boolean isPublic;
}
