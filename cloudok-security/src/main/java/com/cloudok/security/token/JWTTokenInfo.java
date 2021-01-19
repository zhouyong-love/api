package com.cloudok.security.token;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * @author xiazhijian
 * @date Jun 17, 2019 2:14:54 PM
 * 
 */
@Getter @Setter
public class JWTTokenInfo implements Serializable{

	private static final long serialVersionUID = 4792840651435836089L;

	private String key;
	
	private String name;
	
	private TokenType tokenType;  //0 access 1 reflash
	
	public JWTTokenInfo(String key, String name, TokenType tokenType) {
		super();
		this.key = key;
		this.name = name;
		this.tokenType = tokenType;
	}
}
