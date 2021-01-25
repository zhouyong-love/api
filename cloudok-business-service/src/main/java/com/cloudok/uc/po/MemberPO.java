package com.cloudok.uc.po;

import com.cloudok.core.po.PO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberPO extends PO {

	private static final long serialVersionUID = 551896456830133440L;

	
	private String userName;
	
	
	private String email;
	
	
	private String password;
	
	
	private String nickName;
	
	
	private String realName;
	
	
	private java.sql.Date birthDate;
	
	
	private String sex;
	
	
	private String phone;
	
	
	private Long avatar;
	
	private String description;
	
	
}
