package com.cloudok.uc.mapping;

import com.cloudok.core.mapping.Mapping;
import com.cloudok.core.query.QueryOperator;

public class AssociationExperienceMapping extends Mapping {

	private static final long serialVersionUID = 0L;

	public static final Mapping MEMBERID = new Mapping("memberId", "t.member_id", QueryOperator.EQ);

	public static final Mapping ASSOCIATIONID = new Mapping("associationId", "t.association_id");

	public static final Mapping TITLE = new Mapping("title", "t.title");

	public static final Mapping DESCRIPTION = new Mapping("description", "t.description");
	
	public static final Mapping SN = new Mapping("sn", "t.sn");

}
