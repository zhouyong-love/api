package com.cloudok.authority.mapping;

import com.cloudok.core.mapping.Mapping;

public class RoleMapping extends Mapping{

	private static final long serialVersionUID = 0L;
	
	public static final Mapping ROLECODE=new Mapping("roleCode", "t.role_code");
	
	public static final Mapping ROLENAME=new Mapping("roleName", "t.role_name");
	
	public static final Mapping REMARK=new Mapping("remark", "t.remark");
	
	public static final Mapping ROLETYPE=new Mapping("roleType", "t.role_type");
	
}
