package com.cloudok.uc.po;

import com.cloudok.core.po.PO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssociationExperiencePO extends PO {

	private static final long serialVersionUID = 524101104224515840L;

	
	private Long memberId;
	
	
	private Long associationId;
	
	
	private String title;
	
	
	private String description;
	
	private Integer sn;
	
}
