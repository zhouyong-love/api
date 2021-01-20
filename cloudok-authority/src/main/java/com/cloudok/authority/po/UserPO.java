package com.cloudok.authority.po;

import com.cloudok.core.po.PO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPO extends PO {

	private static final long serialVersionUID = 341606631028866100L;

	
	private String userName;
	
	
	private String userFullName;
	
	
	private String password;
	
	
	private String sex;
	
	
	private java.sql.Date birthDay;
	
	
	private String telphone;
	
	
	private String email;
	
	
	private Boolean freeze;
	
	
	private Long avatar;
	
	
	private java.sql.Timestamp lastLoginTime;
	
	
	private String lastLoginAddr;
	
	
}
