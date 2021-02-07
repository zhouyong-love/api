package com.cloudok.uc.mapping;

import com.cloudok.core.mapping.Mapping;

public class FirendMapping extends Mapping{

	private static final long serialVersionUID = 0L;
	
	public static final Mapping SOURCEID=new Mapping("sourceId", "t.source_id");
	
	public static final Mapping TARGETID=new Mapping("targetId", "t.target_id");
	
}
