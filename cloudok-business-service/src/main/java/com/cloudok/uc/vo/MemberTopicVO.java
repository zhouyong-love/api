package com.cloudok.uc.vo;

import com.cloudok.core.vo.VO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberTopicVO extends VO {

	private static final long serialVersionUID = 798287043329233700L;
	
	
	private Long memberId;
	
	
	private Long topicId;
	
	
	private Long lastPostId;
	
	
}
