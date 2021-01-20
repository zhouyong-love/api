package com.cloudok.uc.mapping;

import com.cloudok.core.mapping.Mapping;

public class AssociationExperienceMapping extends Mapping{

	private static final long serialVersionUID = 0L;
	
	public static final Mapping MEMBERID=new Mapping("memberId", "member_id");
	
	public static final Mapping ASSOCIATIONID=new Mapping("associationId", "association_id");
	
	public static final Mapping TITLE=new Mapping("title", "title");
	
	public static final Mapping DESCRIPTION=new Mapping("description", "description");
	
}
