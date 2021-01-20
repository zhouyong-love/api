package com.cloudok.uc.mapping;

import com.cloudok.core.mapping.Mapping;

public class MemberTagsMapping extends Mapping{

	private static final long serialVersionUID = 0L;
	
	public static final Mapping TYPE=new Mapping("type", "type");
	
	public static final Mapping TAGID=new Mapping("tagId", "tag_id");
	
	public static final Mapping MEMBERID=new Mapping("memberId", "member_id");
	
	public static final Mapping WEIGHT=new Mapping("weight", "weight");
	
	public static final Mapping DESCRIPTION=new Mapping("description", "description");
	
}
