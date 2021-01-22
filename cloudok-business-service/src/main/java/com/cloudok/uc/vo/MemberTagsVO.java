package com.cloudok.uc.vo;

import com.cloudok.core.vo.VO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberTagsVO extends VO {

	private static final long serialVersionUID = 218862075040630940L;
	
	
	private Integer type;
	
	
	private Long tagId;
	
	
	private Long memberId;
	
	
	private Integer weight;
	
	
	private String description;
	
	
}