package com.cloudok.base.mapping;

import com.cloudok.core.mapping.Mapping;
import com.cloudok.core.query.QueryOperator;

public class TagMapping extends Mapping{

	private static final long serialVersionUID = 0L;
	
	public static final Mapping NAME=new Mapping("name", "t.name",QueryOperator.LIKE);
	
	public static final Mapping TYPE=new Mapping("type", "t.type",QueryOperator.EQ);
	
	public static final Mapping CATEGORY=new Mapping("category", "t.category",QueryOperator.EQ);
	
	public static final Mapping SUBCATEGORY=new Mapping("subCategory", "t.sub_category",QueryOperator.EQ);
	
	
	public static final Mapping ICON=new Mapping("icon", "t.icon");
	
	public static final Mapping COLOR=new Mapping("color", "t.color");
	
}
