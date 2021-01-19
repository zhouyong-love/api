package com.cloudok.authority.service.grant;

import java.io.Serializable;

public class GrantObjectInfo implements Serializable{

	private static final long serialVersionUID = -8699049468189794061L;
	
	public GrantObjectInfo() {}
	
	public GrantObjectInfo(String code,String name) {
		this.code=code;
		this.name=name;
	}
	
	private String code;
	
	private String name;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
