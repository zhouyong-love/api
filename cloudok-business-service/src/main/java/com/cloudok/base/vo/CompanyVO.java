package com.cloudok.base.vo;

import com.cloudok.core.vo.VO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CompanyVO extends VO {

	private static final long serialVersionUID = 324021789966742140L;

	private String name;

	public CompanyVO(Long id) {
		this.setId(id);
	}

}
