package com.cloudok.enums;

import com.cloudok.base.dict.enums.annotation.Enum;
import com.cloudok.base.dict.enums.EnumInfo;
import com.cloudok.base.dict.enums.annotation.EnumValue;

@Enum(type = "notificationType", label = "消息提醒类型", describe = "消息提醒类型")
public class NotificationType {
	
	@EnumValue(sn = 0)
	public static final EnumInfo comment = new EnumInfo("新评论", "1", "新评论");

	@EnumValue(sn = 1)
	public static final EnumInfo thumbsUp = new EnumInfo("新点赞", "2", "新点赞");

	@EnumValue(sn = 0)
	public static final EnumInfo replyComment = new EnumInfo("评论新回复", "3", "评论新回复");

}
