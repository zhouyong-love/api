package com.cloudok.authority.vo;

import java.util.Map;

import javax.validation.constraints.NotEmpty;

import com.cloudok.core.vo.VO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserVO extends VO {

	private static final long serialVersionUID = 415741698601415300L;
	
	@NotEmpty
	private String userName;
	
	@NotEmpty
	private String passwdHash;
	
	@NotEmpty
	private String firstName;
	
	@NotEmpty
	private String lastName;
	
	@NotEmpty
	private String email;
	
	@NotEmpty
	private String phoneNum;

	@NotEmpty
	private String userType;
	
	
	private String country;
	
	
	private Integer status;
	
	
	private Long avatar;
	
	
	private Boolean freeze;
	
	
	private String sex;
	
	
	private java.sql.Date birthDate;
	
	private Map<String,String> extend;
}
