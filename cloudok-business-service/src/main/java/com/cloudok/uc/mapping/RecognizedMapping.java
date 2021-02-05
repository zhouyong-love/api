package com.cloudok.uc.mapping;

import com.cloudok.core.mapping.Mapping;
import com.cloudok.core.query.QueryOperator;

public class RecognizedMapping extends Mapping{

	private static final long serialVersionUID = 0L;
	
	public static final Mapping SOURCEID=new Mapping("sourceId", "t.source_id",QueryOperator.EQ);
	
	public static final Mapping TARGETID=new Mapping("targetId", "t.target_id",QueryOperator.EQ);
	
	
	public static final Mapping READ=new Mapping("read", "t.read",QueryOperator.EQ);

	
	public static final Mapping READTIME=new Mapping("readTime", "t.read_time",QueryOperator.EQ);

}
