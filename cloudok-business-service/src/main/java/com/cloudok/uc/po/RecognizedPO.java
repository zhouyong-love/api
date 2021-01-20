package com.cloudok.uc.po;

import com.cloudok.core.po.PO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecognizedPO extends PO {

	private static final long serialVersionUID = 155188736287801730L;

	
	private Long sourceId;
	
	
	private Long targetId;
	
	
}
