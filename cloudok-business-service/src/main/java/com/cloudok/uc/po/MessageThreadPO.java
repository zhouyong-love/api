package com.cloudok.uc.po;

import com.cloudok.core.po.PO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageThreadPO extends PO {

	private static final long serialVersionUID = 390784170047036500L;

	
	private String type;
	
	
	private Long ownerId;
	
	
	private Boolean isPublic;
	
	private Long lastMessageId;
	
	
}
