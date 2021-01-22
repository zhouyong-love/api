package com.cloudok.uc.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ForgotVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8296659047962555994L;

	private String email;
	
	private String phone;
	
	private String country;
	
	private String password;
	
	private String code;
	
	private boolean receive;
	
	private String forgotType;

}
