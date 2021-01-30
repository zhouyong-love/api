package com.cloudok.enums;

import com.cloudok.base.dict.enums.annotation.Enum;
import com.cloudok.base.dict.enums.EnumInfo;
import com.cloudok.base.dict.enums.annotation.EnumValue;

@Enum(type = "ucMessageType", label = "UC Message type")
public class UCMessageType {
	@EnumValue(sn = 1)
	public static final EnumInfo recognized = new EnumInfo("认可消息", "1", "认可消息");
	@EnumValue(sn = 2)
	public static final EnumInfo privateMessage = new EnumInfo("私信", "2", "私信");
	@EnumValue(sn = 3)
	public static final EnumInfo privateInteraction = new EnumInfo("匿名互动", "3", "匿名互动");
	@EnumValue(sn = 4)
	public static final EnumInfo publicInteraction = new EnumInfo("实名互动", "4", "实名互动");

}
