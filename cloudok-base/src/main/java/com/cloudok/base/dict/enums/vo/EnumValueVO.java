package com.cloudok.base.dict.enums.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EnumValueVO implements Serializable{

	private static final long serialVersionUID = -2059230624358554130L;

	private String value;
	
	private String label;
	
	private String describe;
	
	private Long sn;
}
