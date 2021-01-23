package com.cloudok.base.vo;

import com.cloudok.core.vo.VO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SchoolSpecialitiesVO extends VO {

	private static final long serialVersionUID = 177369243619615040L;

	private Long schoolId;

	private Long specialismId;

	public SchoolSpecialitiesVO(Long id) {
		this.setId(id);
	}

}
