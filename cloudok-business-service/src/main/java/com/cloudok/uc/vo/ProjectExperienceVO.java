package com.cloudok.uc.vo;

import com.cloudok.core.vo.VO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectExperienceVO extends VO {

	private static final long serialVersionUID = 862813182294686100L;
	
	
	private Long memberId;
	
	
	private String category;
	
	
	private String name;
	
	
	private String job;
	
	
	private String description;
	
	private Integer sn =  0;
	
	
}
