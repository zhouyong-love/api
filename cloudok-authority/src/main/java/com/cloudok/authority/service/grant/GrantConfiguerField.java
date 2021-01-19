package com.cloudok.authority.service.grant;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GrantConfiguerField implements Serializable{

	private static final long serialVersionUID = 9021602594986260052L;

	private String fieldName;
	
	private String columnName;
	
	private boolean searchField;
	
	private String fieldLabel;
	
	private int order;
}
