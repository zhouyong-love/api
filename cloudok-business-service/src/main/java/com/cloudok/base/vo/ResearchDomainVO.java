package com.cloudok.base.vo;

import com.cloudok.core.vo.VO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResearchDomainVO extends VO {

	private static final long serialVersionUID = 398319268252308500L;

	private String name;
	
	private Integer sn = 9999999;

	public ResearchDomainVO(Long id) {
		this.setId(id);
	}

}
