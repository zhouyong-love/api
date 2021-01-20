package com.cloudok.authority.mapping;

import com.cloudok.core.mapping.Mapping;

public class UserMapping extends Mapping{

	private static final long serialVersionUID = 0L;
	
	public static final Mapping USERNAME=new Mapping("userName", "user_name");
	
	public static final Mapping USERFULLNAME=new Mapping("userFullName", "user_full_name");
	
	public static final Mapping PASSWORD=new Mapping("password", "password");
	
	public static final Mapping SEX=new Mapping("sex", "sex");
	
	public static final Mapping BIRTHDAY=new Mapping("birthDay", "birth_day");
	
	public static final Mapping TELPHONE=new Mapping("telphone", "telphone");
	
	public static final Mapping EMAIL=new Mapping("email", "email");
	
	public static final Mapping FREEZE=new Mapping("freeze", "freeze");
	
	public static final Mapping AVATAR=new Mapping("avatar", "avatar");
	
	public static final Mapping LASTLOGINTIME=new Mapping("lastLoginTime", "last_login_time");
	
	public static final Mapping LASTLOGINADDR=new Mapping("lastLoginAddr", "last_login_addr");
	
}
