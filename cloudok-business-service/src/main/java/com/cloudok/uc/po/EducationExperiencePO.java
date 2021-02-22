package com.cloudok.uc.po;

import com.cloudok.core.po.PO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EducationExperiencePO extends PO {

	private static final long serialVersionUID = 881214073804945000L;

	
	private Long memberId;
	
	
	private Long schoolId;
	
	
	private Long specialismId;
	
	
	private Integer grade;
	
	
	private String degree;
	
	private Integer sn;
	
	
}
