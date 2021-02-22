package com.cloudok.enums;

import com.cloudok.base.dict.enums.EnumInfo;
import com.cloudok.base.dict.enums.annotation.Enum;
import com.cloudok.base.dict.enums.annotation.EnumValue;

@Enum(type = "ucMessageThreadType", label = "UC Message type")
public class UCMessageThreadType {
	
	@EnumValue(sn = 1)
	public static final EnumInfo chat = new EnumInfo("私信聊天", "1", "私信聊天");
	
	@EnumValue(sn = 2)
	public static final EnumInfo interaction = new EnumInfo("留言", "2", "留言");
	
	@EnumValue(sn = 3)
	public static final EnumInfo anonymousInteraction = new EnumInfo("匿名留言", "3", "匿名留言");
	 

}
