package com.cloudok.uc.mapping;

import com.cloudok.core.mapping.Mapping;

public class MessageMapping extends Mapping{

	private static final long serialVersionUID = 0L;
	
	public static final Mapping TYPE=new Mapping("type", "t.type");
	
	public static final Mapping FROMID=new Mapping("fromId", "t.from_id");
	
	public static final Mapping TOID=new Mapping("toId", "t.to_id");
	
	public static final Mapping CONTENT=new Mapping("content", "t.content");
	
	public static final Mapping STATUS=new Mapping("status", "t.status");
	
	public static final Mapping STATUSTS=new Mapping("statusTs", "t.status_ts");
	
}
