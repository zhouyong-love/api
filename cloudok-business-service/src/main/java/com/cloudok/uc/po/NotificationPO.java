package com.cloudok.uc.po;

import com.cloudok.core.po.PO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationPO extends PO {

	private static final long serialVersionUID = 873742869596076500L;

	
	private Integer businessType;
	
	
	private Long businessId;
	
	
	private String title;
	
	
	private String remark;
	
	
	private Integer status;
	
	
	private java.sql.Timestamp statusTs;
	
	private Long memberId;
	
	
}
