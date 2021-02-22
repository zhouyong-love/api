package com.cloudok.uc.vo;

import com.cloudok.base.vo.AssociationVO;
import com.cloudok.core.vo.VO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssociationExperienceVO extends VO {

	private static final long serialVersionUID = 195149119636857120L;

	private Long memberId;

	private AssociationVO association;

	private String title;

	private String description;
	
	private Integer sn =  0;

}
