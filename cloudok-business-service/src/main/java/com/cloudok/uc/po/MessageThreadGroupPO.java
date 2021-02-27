package com.cloudok.uc.po;

import lombok.Data;

@Data
public class MessageThreadGroupPO {
	private Long threadId;
	private Long memberId;
	private Long ownerId;
	private Integer type;
}
