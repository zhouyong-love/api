package com.cloudok.authority.vo;

import com.cloudok.core.vo.VO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserVO extends VO {

	private static final long serialVersionUID = 156861730146840220L;
	
	
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
