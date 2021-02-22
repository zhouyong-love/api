package com.cloudok.base.po;

import com.cloudok.core.po.PO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndustryPO extends PO {

	private static final long serialVersionUID = 557577847484976640L;

	
	private String name;
	
	private String category;
	
	private Integer sn;
	
}
