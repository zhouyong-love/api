package com.cloudok.enums;

import com.cloudok.base.dict.enums.EnumInfo;
import com.cloudok.base.dict.enums.annotation.Enum;
import com.cloudok.base.dict.enums.annotation.EnumValue;

@Enum(type = "ucMessageType", label = "UC Message type")
public class UCMessageType {
	@EnumValue(sn = 1)
	public static final EnumInfo recognized = new EnumInfo("认可消息", "1", "认可消息");
	@EnumValue(sn = 2)
	public static final EnumInfo privateMessage = new EnumInfo("私信", "2", "私信");
	
	@EnumValue(sn = 3)
	public static final EnumInfo interaction = new EnumInfo("留言", "3", "留言");
	
	@EnumValue(sn = 4)
	public static final EnumInfo publicInteraction = new EnumInfo("公开回复", "4", "公开回复");
	
	@EnumValue(sn = 4)
	public static final EnumInfo privateInteraction = new EnumInfo("私密回复", "5", "私密回复");

}
