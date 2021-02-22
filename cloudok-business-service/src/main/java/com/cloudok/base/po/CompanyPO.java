package com.cloudok.base.po;

import com.cloudok.core.po.PO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyPO extends PO {

	private static final long serialVersionUID = 575695996244836500L;

	
	private String name;
	
	private Integer sn;
	
}
