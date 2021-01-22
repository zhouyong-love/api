package com.cloudok.base.message.mapping;

import com.cloudok.core.mapping.Mapping;

public class MessageDetailsMapping extends Mapping{

	private static final long serialVersionUID = 0L;
	
	public static final Mapping MESSAGEID=new Mapping("messageId", "t.message_id");
	
	public static final Mapping USERID=new Mapping("userId", "t.user_id");
	
	public static final Mapping USERNAME=new Mapping("userName", "t.user_name");
	
	public static final Mapping RECEIVER=new Mapping("receiver", "t.receiver");
	
	public static final Mapping RECEIVERTYPE=new Mapping("receiverType", "t.receiver_type");
	
	public static final Mapping RESENDCOUNT=new Mapping("resendCount", "t.resend_count");
	
	public static final Mapping STATUS=new Mapping("status", "status");
	
	public static final Mapping CALLMESSAGE=new Mapping("callMessage", "t.call_message");
	
}
