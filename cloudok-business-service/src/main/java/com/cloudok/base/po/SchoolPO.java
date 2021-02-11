package com.cloudok.base.po;

import com.cloudok.core.po.PO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SchoolPO extends PO {

	private static final long serialVersionUID = 353569462655600500L;

	
	private String name;
	
	
	private String emailPostfix;
	
	private String abbreviation;
	
	private Integer sn;
	
	
}
