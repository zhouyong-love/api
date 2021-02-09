package com.cloudok.log.po;

import com.cloudok.core.po.PO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysLogPO extends PO {

	private static final long serialVersionUID = 120453793687414380L;

	
	private String modelCode;
	
	
	private String modelName;
	
	
	private String interfaceCode;
	
	
	private String interfaceName;
	
	
	private Long userId;
	
	
	private String userName;
	
	
	private String requestUrl;
	
	
	private String requestMethod;
	
	
	private String input;
	
	
	private Integer inputLogSwitch;
	
	
	private String logLevel;
	
	
	private Integer outputLogSwitch;
	
	
	private String output;
	
	
	private Boolean status;
	
	
	private String formatedMessage;
	
	
	private Integer httpCode;
	
	
	private Long businessId;
	
	
	private Long relationBusinessId;
	
	
}
