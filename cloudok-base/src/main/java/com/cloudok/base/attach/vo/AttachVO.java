package com.cloudok.base.attach.vo;

import com.cloudok.core.vo.VO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttachVO extends VO {

	private static final long serialVersionUID = 380759807796574800L;
	
	
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
	
	private String url;
	
	public AttachVO() {
		
	}
	public AttachVO(Long id) {
		this.setId(id);
	}
}
