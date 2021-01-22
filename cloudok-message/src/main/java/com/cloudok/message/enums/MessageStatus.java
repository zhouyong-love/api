package com.cloudok.message.enums;

import com.cloudok.base.enums.EnumInfo;
import com.cloudok.base.enums.annotation.Enum;
import com.cloudok.base.enums.annotation.EnumValue;

@Enum(type = "messageStatus", label = "消息类型", describe = "消息类型")
public class MessageStatus {
	@EnumValue(sn = 1)
	public static final EnumInfo UNREAD = new EnumInfo("未读", "0", "未读");
	@EnumValue(sn = 2)
	public static final EnumInfo READ = new EnumInfo("已读", "1", "已读");
}
