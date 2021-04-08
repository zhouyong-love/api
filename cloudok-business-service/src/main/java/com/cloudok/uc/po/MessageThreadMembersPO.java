package com.cloudok.uc.po;

import com.cloudok.core.po.PO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageThreadMembersPO extends PO {

	private static final long serialVersionUID = 578312452443968900L;

	
	private Long threadId;
	
	private Long memberId;
	
	private Long lastPosition;
	
	private Integer unRead;
}
