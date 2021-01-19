package com.cloudok.base.po;

import com.cloudok.core.po.PO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttachPO extends PO {

	private static final long serialVersionUID = 685594098595714300L;

	
	private String storageModel;
	
	
	private String address;
	
	
	private Long businessId;
	
	
	private String businessKey;
	
	
	private Boolean used;
	
	
	private String fileName;
	
	
	private String suffix;
	
	
	private Long fileSize;
	
	
	private String fileType;
	
	
	private String businessRemark;
	
	
}
