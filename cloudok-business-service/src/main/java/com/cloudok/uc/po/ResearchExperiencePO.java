package com.cloudok.uc.po;

import com.cloudok.core.po.PO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResearchExperiencePO extends PO {

	private static final long serialVersionUID = 389816720913764600L;

	
	private Long memberId;
	
	
	private Long domainId;
	
	
	private String name;
	
	
	private String description;
	
	private Integer sn;
	
}
