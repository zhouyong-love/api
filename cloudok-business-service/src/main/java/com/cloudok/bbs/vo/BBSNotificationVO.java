package com.cloudok.bbs.vo;

import com.cloudok.base.attach.vo.AttachVO;
import com.cloudok.core.vo.VO;
import com.cloudok.uc.dto.SimpleMemberInfo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BBSNotificationVO extends VO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4061720035582923488L;
	
	private SimpleMemberInfo memberInfo;
	
	private Integer type; // 1 评论  2 点赞
	
	private Long postId; 
	
	private String comment; //评论内容
	 
	private String content; //动态内容
	
	private AttachVO photo; //动态的第一个图
	 
	

}
