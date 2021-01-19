package com.cloudok.security.token;

/**
 * @author xiazhijian
 * @date Jun 17, 2019 2:29:21 PM
 * 
 */
public enum TokenType {

	ACCESS(0),REFRESH(1);
	
	private int type;
	
	private TokenType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
