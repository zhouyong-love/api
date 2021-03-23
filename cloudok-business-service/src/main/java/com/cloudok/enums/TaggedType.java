package com.cloudok.enums;

import com.cloudok.base.dict.enums.annotation.Enum;
import com.cloudok.base.dict.enums.EnumInfo;
import com.cloudok.base.dict.enums.annotation.EnumValue;

@Enum(type = "taggedType", label = "标签标记类型", describe = "标签标记类型")
public class TaggedType {
	@EnumValue(sn = 1)
	public static final EnumInfo SYSTEM = new EnumInfo("系统标记", "1", "系统标记");
	@EnumValue(sn = 2)
	public static final EnumInfo CUSTOM = new EnumInfo("用户自我标定", "2", "用户自我标定");
	@EnumValue(sn = 3)
	public static final EnumInfo POST = new EnumInfo("动态关联的标签", "3", "动态关联的标签");
}
