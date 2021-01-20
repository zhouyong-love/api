package com.cloudok.base.po;

import com.cloudok.core.po.PO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagPO extends PO {

	private static final long serialVersionUID = 396897083472089100L;

	
	private String name;
	
	
	private Integer type;
	
	
	private String category;
	
	
	private Long icon;
	
	
	private String color;
	
	
}
