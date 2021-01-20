package com.cloudok.uc.vo;

import com.cloudok.core.vo.VO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InternshipExperienceVO extends VO {

	private static final long serialVersionUID = 53458632939655140L;
	
	
	private Long memberId;
	
	
	private Long companyId;
	
	
	private Long jobId;
	
	
	private Long industryId;
	
	
	private String description;
	
	
}
