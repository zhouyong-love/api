package com.cloudok.uc.po;

import com.cloudok.core.po.PO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberTopicPO extends PO {

	private static final long serialVersionUID = 818357277443142400L;

	
	private Long memberId;
	
	
	private Long topicId;
	
	
	private Long lastPostId;
	
	
}
