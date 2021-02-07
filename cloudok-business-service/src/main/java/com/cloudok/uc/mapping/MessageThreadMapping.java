package com.cloudok.uc.mapping;

import com.cloudok.core.mapping.Mapping;

public class MessageThreadMapping extends Mapping{

	private static final long serialVersionUID = 0L;
	
	public static final Mapping TYPE=new Mapping("type", "t.type");
	
	public static final Mapping OWERID=new Mapping("owerId", "t.ower_id");
	
	public static final Mapping ISPUBLIC=new Mapping("isPublic", "t.is_public");
	
	public static final Mapping LASTMESSAGEID=new Mapping("lastMessageId", "t.last_message_id");
	
}
