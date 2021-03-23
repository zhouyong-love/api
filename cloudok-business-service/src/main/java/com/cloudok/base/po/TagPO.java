package com.cloudok.base.po;

import com.cloudok.core.po.PO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagPO extends PO {

	private static final long serialVersionUID = 396897083472089100L;

	private Long parentId;

	private String name;

	private String type;

	private String category;

	private String icon;

	private String color;
	
	private Integer sn;

	private Long relationTo;
}
