package com.cloudok.authority.vo;

import com.cloudok.core.vo.VO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleVO extends VO {

	private static final long serialVersionUID = 557655102869047040L;
	
	
	private String roleCode;
	
	
	private String roleName;
	
	
	private String remark;
	
	
	private String roleType;
	
	
}
