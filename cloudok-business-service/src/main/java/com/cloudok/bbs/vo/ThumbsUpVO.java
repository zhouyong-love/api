package com.cloudok.bbs.vo;

import com.cloudok.core.vo.VO;
import com.cloudok.uc.dto.SimpleMemberInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ThumbsUpVO extends VO {

	private static final long serialVersionUID = 398679823110439360L;
	
	
	private Integer businessType;
	
	
	private Long businessId;
	

	private SimpleMemberInfo memberInfo;
	
}
