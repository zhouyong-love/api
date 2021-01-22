package com.cloudok.base.message.po;

import com.cloudok.core.po.PO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDetailsPO extends PO {

	private static final long serialVersionUID = 136414291151959020L;

	
	private Long messageId;
	
	
	private Long userId;
	
	
	private String userName;
	
	
	private String receiver;
	
	
	private String receiverType;
	
	
	private Integer resendCount;
	
	
	private Integer status;
	
	
	private String callMessage;
	
	
}
