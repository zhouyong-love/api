package com.cloudok.core.enums;

public enum UserType {
	SYS_USER("SysUser"),MEMBER("Member");
	private String type;

	UserType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}
}
