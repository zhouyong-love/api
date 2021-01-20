package com.cloudok.base.vo;

import com.cloudok.core.vo.VO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagVO extends VO {

	private static final long serialVersionUID = 85983087007446940L;
	
	
	private String name;
	
	
	private Integer type;
	
	
	private String category;
	
	
	private Long icon;
	
	
	private String color;
	
	
}
