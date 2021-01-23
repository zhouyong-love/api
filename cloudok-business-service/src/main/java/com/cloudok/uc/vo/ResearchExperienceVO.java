package com.cloudok.uc.vo;

import com.cloudok.base.vo.ResearchDomainVO;
import com.cloudok.core.vo.VO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResearchExperienceVO extends VO {

	private static final long serialVersionUID = 959507043423369200L;
	
	
	private Long memberId;
	
	
	private ResearchDomainVO domain;
	
	
	private String name;
	
	
	private String description;
	
	
}
