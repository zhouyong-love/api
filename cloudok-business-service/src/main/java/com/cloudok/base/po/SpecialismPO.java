package com.cloudok.base.po;

import com.cloudok.core.po.PO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpecialismPO extends PO {

	private static final long serialVersionUID = 613769102515416400L;

	
	private String name;
	
	
	private String category;
	
	private Integer sn;
	
}
