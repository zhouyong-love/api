package com.cloudok.uc.vo;

import com.cloudok.base.vo.CompanyVO;
import com.cloudok.base.vo.IndustryVO;
import com.cloudok.base.vo.JobVO;
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
public class InternshipExperienceVO extends VO {

	private static final long serialVersionUID = 53458632939655140L;

	private Long memberId;

	private CompanyVO company;

	private JobVO job;

	private IndustryVO industry;

	private String description;

	private Integer sn =  0;
}
