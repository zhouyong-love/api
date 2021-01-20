package com.cloudok.uc.vo;

import com.cloudok.core.vo.VO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectExperienceVO extends VO {

	private static final long serialVersionUID = 862813182294686100L;
	
	
	private Long memberId;
	
	
	private String category;
	
	
	private String name;
	
	
	private String job;
	
	
	private String description;
	
	
}
