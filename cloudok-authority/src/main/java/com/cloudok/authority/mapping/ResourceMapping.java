package com.cloudok.authority.mapping;

import com.cloudok.core.mapping.Mapping;

public class ResourceMapping extends Mapping{

	private static final long serialVersionUID = 0L;
	
	public static final Mapping RESOURCECODE=new Mapping("resourceCode", "t.resource_code");
	
	public static final Mapping RESOURCENAME=new Mapping("resourceName", "t.resource_name");
	
	public static final Mapping PARENTID=new Mapping("parentId", "t.parent_id");
	
	public static final Mapping RESOURCEURL=new Mapping("resourceUrl", "t.resource_url");
	
	public static final Mapping RESOURCEICON=new Mapping("resourceIcon", "t.resource_icon");
	
	public static final Mapping RESOURCEPATH=new Mapping("resourcePath", "t.resource_path");
	
	public static final Mapping RESOURCETYPE=new Mapping("resourceType", "t.resource_type");
	
	public static final Mapping REMARK=new Mapping("remark", "t.remark");
	
	public static final Mapping SN=new Mapping("sn", "t.sn");
	
}
