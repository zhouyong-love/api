package com.cloudok.uc.po;

import com.cloudok.core.po.PO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectExperiencePO extends PO {

	private static final long serialVersionUID = 921941777716819100L;

	
	private Long memberId;
	
	
	private String category;
	
	
	private String name;
	
	
	private String job;
	
	
	private String description;
	
	private Integer sn;
	
	
}
