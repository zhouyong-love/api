package com.cloudok.uc.mapping;

import com.cloudok.core.mapping.Mapping;

public class MessageThreadMembersMapping extends Mapping{

	private static final long serialVersionUID = 0L;
	
	public static final Mapping THREADID=new Mapping("threadId", "t.thread_id");
	
	public static final Mapping MEMBERID=new Mapping("memberId", "t.member_id");
	
	public static final Mapping LASTPOSITION=new Mapping("lastPosition", "t.last_position");
	
}
