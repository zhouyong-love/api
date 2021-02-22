package com.cloudok.uc.po;

import com.cloudok.core.po.PO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberTagsPO extends PO {

	private static final long serialVersionUID = 517669335596008900L;

	
	private Integer type;
	
	
	private Long tagId;
	
	
	private Long memberId;
	
	
	private Integer weight;
	
	
	private String description;
	
	private Integer sn;
	
}
