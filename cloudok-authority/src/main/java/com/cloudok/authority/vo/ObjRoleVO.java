package com.cloudok.authority.vo;

import com.cloudok.core.vo.VO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObjRoleVO extends VO {

	private static final long serialVersionUID = 839228710188745500L;
	
	
	private Long roleId;
	
	
	private Long objId;
	
	
	private String objType;
	
	
}
