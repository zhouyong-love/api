package com.cloudok.uc.mapping;

import com.cloudok.core.mapping.Mapping;

public class MemberMapping extends Mapping{

	private static final long serialVersionUID = 0L;
	
	public static final Mapping USERNAME=new Mapping("userName", "user_name");
	
	public static final Mapping EMAIL=new Mapping("email", "email");
	
	public static final Mapping PASSWORD=new Mapping("password", "password");
	
	public static final Mapping NICKNAME=new Mapping("nickName", "nick_name");
	
	public static final Mapping REALNAME=new Mapping("realName", "real_name");
	
	public static final Mapping BIRTHDATE=new Mapping("birthDate", "birth_date");
	
	public static final Mapping SEX=new Mapping("sex", "sex");
	
	public static final Mapping PHONE=new Mapping("phone", "phone");
	
	public static final Mapping AVATAR=new Mapping("avatar", "avatar");
	
}
