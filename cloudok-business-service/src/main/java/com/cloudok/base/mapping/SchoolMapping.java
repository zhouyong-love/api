package com.cloudok.base.mapping;

import com.cloudok.core.mapping.Mapping;
import com.cloudok.core.query.QueryOperator;

public class SchoolMapping extends Mapping {

	private static final long serialVersionUID = 0L;

	public static final Mapping NAME = new Mapping("name", "t.name", QueryOperator.LIKE);

	public static final Mapping EMAILPOSTFIX = new Mapping("emailPostfix", "t.email_postfix");
	
	public static final Mapping ABBREVIATION = new Mapping("abbreviation", "t.abbreviation");
	
	public static final Mapping SN = new Mapping("sn", "t.sn");

}
