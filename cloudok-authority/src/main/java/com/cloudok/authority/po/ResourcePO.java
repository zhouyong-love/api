package com.cloudok.authority.po;

import com.cloudok.core.po.PO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourcePO extends PO {

	private static final long serialVersionUID = 374424668568496640L;

	
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
