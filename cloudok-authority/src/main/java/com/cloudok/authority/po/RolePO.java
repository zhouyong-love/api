package com.cloudok.authority.po;

import com.cloudok.core.po.PO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RolePO extends PO {

	private static final long serialVersionUID = 607642420537219100L;

	
	private String roleCode;
	
	
	private String roleName;
	
	
	private String remark;
	
	
	private String roleType;
	
	
}
