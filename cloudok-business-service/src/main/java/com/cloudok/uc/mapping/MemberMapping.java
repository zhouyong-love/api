package com.cloudok.uc.mapping;

import com.cloudok.core.mapping.Mapping;
import com.cloudok.core.query.QueryOperator;

public class MemberMapping extends Mapping {

	private static final long serialVersionUID = 0L;

	public static final Mapping USERNAME = new Mapping("userName", "t.user_name", QueryOperator.LIKE);

	public static final Mapping EMAIL = new Mapping("email", "t.email", QueryOperator.LIKE);

	public static final Mapping PASSWORD = new Mapping("password", "t.password");

	public static final Mapping NICKNAME = new Mapping("nickName", "t.nick_name", QueryOperator.LIKE);

	public static final Mapping REALNAME = new Mapping("realName", "t.real_name", QueryOperator.LIKE);

	public static final Mapping BIRTHDATE = new Mapping("birthDate", "t.birth_date", QueryOperator.LIKE);

	public static final Mapping SEX = new Mapping("sex", "t.sex", QueryOperator.EQ);

	public static final Mapping PHONE = new Mapping("phone", "t.phone", QueryOperator.LIKE);

	public static final Mapping AVATAR = new Mapping("avatar", "t.avatar");
	
	public static final Mapping REMARK = new Mapping("remark", "t.remark");

	public static final Mapping STATE = new Mapping("state", "t.state");
	
	public static final Mapping WI = new Mapping("wi", "t.wi");
	
	public static final Mapping TI = new Mapping("ti", "t.ti");
	
	public static final Mapping profileUpdateTs = new Mapping("profileUpdateTs", "t.profile_update_ts");
	
	public static final Mapping openId = new Mapping("openId", "t.open_id");

}
