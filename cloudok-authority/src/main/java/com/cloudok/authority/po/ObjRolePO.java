package com.cloudok.authority.po;

import com.cloudok.core.po.PO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObjRolePO extends PO {

	private static final long serialVersionUID = 958858736147501200L;

	
	private Long roleId;
	
	
	private Long objId;
	
	
	private String objType;
	
	
}
