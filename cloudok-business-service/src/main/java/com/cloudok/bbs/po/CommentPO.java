package com.cloudok.bbs.po;

import com.cloudok.core.po.PO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentPO extends PO {

	private static final long serialVersionUID = 740277632950403700L;

	private String content;

	private Long postId;

	private String path;

	private Long parentId;

//	@Deprecated
//	private Integer status;
//
//	@Deprecated
//	private Timestamp statusTs;
	
	private Long replyTo;
	 

}
