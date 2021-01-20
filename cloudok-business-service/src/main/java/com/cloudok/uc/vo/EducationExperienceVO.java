package com.cloudok.uc.vo;

import com.cloudok.core.vo.VO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EducationExperienceVO extends VO {

	private static final long serialVersionUID = 256594519618572160L;
	
	
	private Long memberId;
	
	
	private Long schoolId;
	
	
	private Long specialismId;
	
	
	private Integer grade;
	
	
	private String degree;
	
	
}
