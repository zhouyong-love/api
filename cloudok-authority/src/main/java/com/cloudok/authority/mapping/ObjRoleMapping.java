package com.cloudok.authority.mapping;

import com.cloudok.core.mapping.Mapping;

public class ObjRoleMapping extends Mapping{

	private static final long serialVersionUID = 0L;
	
	public static final Mapping ROLEID=new Mapping("roleId", "t.role_id");
	
	public static final Mapping OBJID=new Mapping("objId", "t.obj_id");
	
	public static final Mapping OBJTYPE=new Mapping("objType", "t.obj_type");
	
}
