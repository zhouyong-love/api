package com.cloudok.uc.mapping;

import com.cloudok.core.mapping.Mapping;
import com.cloudok.core.query.QueryOperator;

public class ResearchExperienceMapping extends Mapping {

	private static final long serialVersionUID = 0L;

	public static final Mapping MEMBERID = new Mapping("memberId", "t.member_id", QueryOperator.EQ);

	public static final Mapping DOMAINID = new Mapping("domainId", "t.domain_id", QueryOperator.EQ);

	public static final Mapping NAME = new Mapping("name", "t.name");

	public static final Mapping DESCRIPTION = new Mapping("description", "t.description");
	
	public static final Mapping SN = new Mapping("sn", "t.sn");

}
