package com.cloudok.base.message.po;

import com.cloudok.core.po.PO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessagePO extends PO {

	private static final long serialVersionUID = 878076530906545700L;

	
	private String messageType;
	
	
	private String messageContent;
	
	
	private String title;
	
	
	private Integer status;
	
	
	private String params;
	
	
	private String userName;
	
	
}
