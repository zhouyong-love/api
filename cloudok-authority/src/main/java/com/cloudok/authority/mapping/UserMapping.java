package com.cloudok.authority.mapping;

import com.cloudok.core.mapping.Mapping;
import com.cloudok.core.query.QueryOperator;

public class UserMapping extends Mapping {

	private static final long serialVersionUID = 0L;

	public static final Mapping USERNAME = new Mapping("userName", "t.user_name", QueryOperator.LIKE);

	public static final Mapping USERFULLNAME = new Mapping("userFullName", "t.user_full_name", QueryOperator.LIKE);

	public static final Mapping PASSWORD = new Mapping("password", "t.password");

	public static final Mapping SEX = new Mapping("sex", "t.sex", QueryOperator.EQ);

	public static final Mapping BIRTHDAY = new Mapping("birthDay", "t.birth_day");

	public static final Mapping TELPHONE = new Mapping("telphone", "t.telphone", QueryOperator.LIKE);

	public static final Mapping EMAIL = new Mapping("email", "t.email", QueryOperator.LIKE);

	public static final Mapping FREEZE = new Mapping("freeze", "t.freeze", QueryOperator.EQ);

	public static final Mapping AVATAR = new Mapping("avatar", "t.avatar");

	public static final Mapping LASTLOGINTIME = new Mapping("lastLoginTime", "t.last_login_time");

	public static final Mapping LASTLOGINADDR = new Mapping("lastLoginAddr", "t.last_login_addr");

}
