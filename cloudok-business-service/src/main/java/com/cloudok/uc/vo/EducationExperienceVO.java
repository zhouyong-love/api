package com.cloudok.uc.vo;

import com.cloudok.base.vo.SchoolVO;
import com.cloudok.base.vo.SpecialismVO;
import com.cloudok.core.vo.VO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EducationExperienceVO extends VO {

	private static final long serialVersionUID = 256594519618572160L;

	private Long memberId;

	private SchoolVO school;

	private SpecialismVO specialism;

	private Integer grade;

	private String degree;
	
	private Integer sn =  0;

}
