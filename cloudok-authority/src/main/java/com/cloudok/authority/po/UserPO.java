package com.cloudok.authority.po;

import com.cloudok.authority.service.grant.GrantObjectField;
import com.cloudok.core.po.PO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPO extends PO {

	private static final long serialVersionUID = 559914267692421400L;

	@GrantObjectField(fieldLabel = "Login name",searchField = true,order = 0)
	private String userName;
	
	
	private String passwdHash;
	
	@GrantObjectField(fieldLabel = "First name",searchField = true,order = 1)
	private String firstName;
	
	@GrantObjectField(fieldLabel = "Last name",searchField = true,order = 2)
	private String lastName;
	
	@GrantObjectField(fieldLabel = "Emain",searchField = true,order = 3)
	private String email;
	
	@GrantObjectField(fieldLabel = "Phone number",searchField = true,order = 4)
	private String phoneNum;
	
	private String userType;

	private String country;
	
	
	private Integer status;
	
	
	private Long avatar;
	
	
	private Boolean freeze;
	
	
	private String sex;
	
	
	private java.sql.Date birthDate;
	
	
}
