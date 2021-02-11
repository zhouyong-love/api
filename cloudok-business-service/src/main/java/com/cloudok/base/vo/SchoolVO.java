package com.cloudok.base.vo;

import com.cloudok.core.vo.VO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SchoolVO extends VO {

	private static final long serialVersionUID = 102927756003409200L;
	
	
	private String name;
	
	
	private String emailPostfix;
	
	private String abbreviation;
	
	private Integer sn;
	
	public SchoolVO(Long id) {
		this.setId(id);
	}
	
	
}
