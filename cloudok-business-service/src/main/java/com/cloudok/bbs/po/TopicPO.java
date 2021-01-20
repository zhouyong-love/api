package com.cloudok.bbs.po;

import com.cloudok.core.po.PO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopicPO extends PO {

	private static final long serialVersionUID = 28309518636283172L;

	
	private String name;
	
	
	private Long icon;
	
	
	private Integer status;
	
	
	private Integer postCount;
	
	
	private Integer type;
	
	
}
