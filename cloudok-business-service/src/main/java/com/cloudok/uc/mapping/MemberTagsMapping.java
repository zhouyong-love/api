package com.cloudok.uc.mapping;

import com.cloudok.core.mapping.Mapping;
import com.cloudok.core.query.QueryOperator;

public class MemberTagsMapping extends Mapping {

	private static final long serialVersionUID = 0L;

	public static final Mapping TYPE = new Mapping("type", "t.type", QueryOperator.EQ);

	public static final Mapping TAGID = new Mapping("tagId", "t.tag_id", QueryOperator.EQ);

	public static final Mapping MEMBERID = new Mapping("memberId", "t.member_id", QueryOperator.EQ);

	public static final Mapping WEIGHT = new Mapping("weight", "t.weight");

	public static final Mapping DESCRIPTION = new Mapping("description", "t.description");
	
	public static final Mapping SN = new Mapping("sn", "t.sn");

}
