package com.cloudok.bbs.po;

import com.cloudok.core.po.PO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BBSNotificationPO extends PO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6543001217750593597L;

	private Integer type; // 1 评论  2 点赞
	
	private Long postId; 
	
	private Long businssId; // 业务主键id
	
	private String comment; //评论内容
	 
	private String content; //动态内容
	
	private String attachIds; //动态的第一个图
	
	private Long memberId;
}
