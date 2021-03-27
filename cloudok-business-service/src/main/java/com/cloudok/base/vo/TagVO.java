package com.cloudok.base.vo;

import com.cloudok.core.vo.VO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TagVO extends VO {

	private static final long serialVersionUID = 85983087007446940L;

	private Long parentId;

	private String name;

	private String type;

	private String category;

	private String icon;

	private String color;
	
	private Integer sn = 9999999;
	
	private Long relationTo;

	public TagVO(Long id) {
		this.setId(id);
	}
	 

}
