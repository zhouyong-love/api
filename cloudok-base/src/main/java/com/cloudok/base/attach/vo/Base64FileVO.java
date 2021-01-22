package com.cloudok.base.attach.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Base64FileVO implements Serializable{

	private static final long serialVersionUID = 2627242671668060874L;

	private String fileName;
	
	private String base64;

}
