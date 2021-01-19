package com.cloudok.authority.vo;

import com.cloudok.core.vo.VO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceVO extends VO {

	private static final long serialVersionUID = 817132839065978600L;
	
	
	private String resourceCode;
	
	
	private String resourceName;
	
	
	private Long parentId;
	
	
	private String resourceUrl;
	
	
	private String resourceIcon;
	
	
	private String resourcePath;
	
	
	private String resourceType;
	
	
	private String remark;
	
	
	private Long sn;
	
	
}
