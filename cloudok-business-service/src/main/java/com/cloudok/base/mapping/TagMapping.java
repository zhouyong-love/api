package com.cloudok.base.mapping;

import com.cloudok.core.mapping.Mapping;

public class TagMapping extends Mapping{

	private static final long serialVersionUID = 0L;
	
	public static final Mapping NAME=new Mapping("name", "name");
	
	public static final Mapping TYPE=new Mapping("type", "type");
	
	public static final Mapping CATEGORY=new Mapping("category", "category");
	
	public static final Mapping ICON=new Mapping("icon", "icon");
	
	public static final Mapping COLOR=new Mapping("color", "color");
	
}
