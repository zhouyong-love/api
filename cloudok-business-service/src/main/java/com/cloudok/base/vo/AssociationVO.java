package com.cloudok.base.vo;

import com.cloudok.core.vo.VO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AssociationVO extends VO {

	private static final long serialVersionUID = 794037239236102100L;

	private String name;

	public AssociationVO(Long id) {
		this.setId(id);
	}

}
