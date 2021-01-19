package com.cloudok.authority.mapping;

import com.cloudok.core.mapping.Mapping;

public class RoleResMapping extends Mapping{

	private static final long serialVersionUID = 0L;
	
	public static final Mapping RESID=new Mapping("resId", "t.res_id");
	
	public static final Mapping ROLEID=new Mapping("roleId", "t.role_id");
	
}
