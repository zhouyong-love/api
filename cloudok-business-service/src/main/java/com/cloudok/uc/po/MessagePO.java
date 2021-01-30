package com.cloudok.uc.po;

import com.cloudok.core.po.PO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessagePO extends PO {

	private static final long serialVersionUID = 7364957682101325L;

	
	private Boolean type;
	
	
	private Long fromId;
	
	
	private Long toId;
	
	
	private String content;
	
	
	private Boolean status;
	
	
	private java.sql.Timestamp statusTs;
	
	
}
