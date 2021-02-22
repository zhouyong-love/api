package com.cloudok.base.po;

import com.cloudok.core.po.PO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobPO extends PO {

	private static final long serialVersionUID = 35169256600910524L;

	
	private String name;
	
	private Integer sn;
}
