package com.cloudok.base.vo;

import com.cloudok.core.vo.VO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SpecialismVO extends VO {

	private static final long serialVersionUID = 240116797552618620L;
	
	
	private String name;
	
	
	private String category;
	
	private Integer sn = 9999999;
	

	public SpecialismVO(Long id) {
		this.setId(id);
	}
	
}
