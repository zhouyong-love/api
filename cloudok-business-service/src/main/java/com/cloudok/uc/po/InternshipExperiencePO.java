package com.cloudok.uc.po;

import com.cloudok.core.po.PO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InternshipExperiencePO extends PO {

	private static final long serialVersionUID = 913881797518204000L;

	
	private Long memberId;
	
	
	private Long companyId;
	
	
	private Long jobId;
	
	
	private Long industryId;
	
	
	private String description;
	
	private Integer sn;
}
