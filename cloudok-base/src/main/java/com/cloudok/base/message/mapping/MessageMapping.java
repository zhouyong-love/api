package com.cloudok.base.message.mapping;

import com.cloudok.core.mapping.Mapping;
import com.cloudok.core.query.QueryOperator;

public class MessageMapping extends Mapping {

	private static final long serialVersionUID = 0L;

	public static final Mapping MESSAGETYPE = new Mapping("messageType", "t.message_type", QueryOperator.EQ);

	public static final Mapping MESSAGECONTENT = new Mapping("messageContent", "t.message_content");

	public static final Mapping TITLE = new Mapping("title", "t.title", QueryOperator.LIKE);

	public static final Mapping STATUS = new Mapping("status", "t.status", QueryOperator.EQ);

	public static final Mapping PARAMS = new Mapping("params", "t.params");

	public static final Mapping USERNAME = new Mapping("userName", "t.user_name");

	public static final Mapping USERID = new Mapping("userId", "t1.user_id");
	
	public static final Mapping RECEIVER = new Mapping("receiver", "t1.receiver");

}
