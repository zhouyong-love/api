package com.cloudok.base.mapping;

import com.cloudok.core.mapping.Mapping;

public class SchoolMapping extends Mapping{

	private static final long serialVersionUID = 0L;
	
	public static final Mapping NAME=new Mapping("name", "name");
	
	public static final Mapping EMAILPOSTFIX=new Mapping("emailPostfix", "email_postfix");
	
}
