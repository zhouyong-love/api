package com.cloudok.uc.vo;

import com.cloudok.core.vo.VO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberVO extends VO {

	private static final long serialVersionUID = 212382535356088200L;
	
	
	private String userName;
	
	
	private String email;
	
	
	private String password;
	
	
	private String nickName;
	
	
	private String realName;
	
	
	private java.sql.Date birthDate;
	
	
	private String sex;
	
	
	private String phone;
	
	
	private Long avatar;
	
	
}
