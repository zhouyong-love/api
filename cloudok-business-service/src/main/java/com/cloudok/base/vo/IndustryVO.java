package com.cloudok.base.vo;

import com.cloudok.core.vo.VO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IndustryVO extends VO {

	private static final long serialVersionUID = 143765078230832620L;
	
	
	private String name;
	
	private String category;
	
	private Integer sn = 9999999;
	

	public IndustryVO(Long id) {
		this.setId(id);
	}
}
