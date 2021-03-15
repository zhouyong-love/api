package com.cloudok.uc.vo;

import com.cloudok.core.vo.VO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationVO extends VO {

	private static final long serialVersionUID = 708576325166700500L;
	
	
	private Integer businessType;
	
	
	private Long businessId;
	
	
	private String title;
	
	
	private String remark;
	
	
	private Integer status;
	
	
	private java.sql.Timestamp statusTs;
	
	private Long memberId;
	
	
}
