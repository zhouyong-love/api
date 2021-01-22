package com.cloudok.base.message.vo;

import com.cloudok.core.vo.VO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDetailsVO extends VO {

	private static final long serialVersionUID = 176848325065502850L;
	
	
	private Long messageId;
	
	
	private Long userId;
	
	
	private String userName;
	
	
	private String receiver;
	
	
	private String receiverType;
	
	
	private Integer resendCount;
	
	
	private Integer status;
	
	
	private String callMessage;
	
	
}
