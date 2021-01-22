package com.cloudok.base.dict.enums.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class EnumVO implements Serializable{

	private static final long serialVersionUID = -648942460755230990L;

	private String type;
	
	private String label;
	
	private String describ;
	
	private List<EnumValueVO> values;
}
