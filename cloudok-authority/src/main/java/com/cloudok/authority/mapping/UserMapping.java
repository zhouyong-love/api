package com.cloudok.authority.mapping;

import com.cloudok.core.mapping.Mapping;

public class UserMapping extends Mapping{

	private static final long serialVersionUID = 0L;
	
	public static final Mapping USERNAME=new Mapping("userName", "t.user_name");
	
	public static final Mapping PASSWDHASH=new Mapping("passwdHash", "t.passwd_hash");
	
	public static final Mapping FIRSTNAME=new Mapping("firstName", "t.first_name");
	
	public static final Mapping LASTNAME=new Mapping("lastName", "t.last_name");
	
	public static final Mapping EMAIL=new Mapping("email", "t.email");

	public static final Mapping USERTYPE=new Mapping("userType", "t.user_type");

	public static final Mapping PHONENUM=new Mapping("phoneNum", "t.phone_num");
	
	public static final Mapping COUNTRY=new Mapping("country", "t.country");
	
	public static final Mapping STATUS=new Mapping("status", "t.status");
	
	public static final Mapping AVATAR=new Mapping("avatar", "avatar");
	
	public static final Mapping FREEZE=new Mapping("freeze", "t.freeze");
	
	public static final Mapping SEX=new Mapping("sex", "t.sex");
	
	public static final Mapping BIRTHDATE=new Mapping("birthDate", "t.birth_date");
	
}
