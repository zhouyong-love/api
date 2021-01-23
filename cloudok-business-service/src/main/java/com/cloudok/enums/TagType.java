package com.cloudok.enums;

import com.cloudok.base.dict.enums.annotation.Enum;
import com.cloudok.base.dict.enums.EnumInfo;
import com.cloudok.base.dict.enums.annotation.EnumValue;

@Enum(type = "tagType", label = "标签类型", describe = "标签类型")
public class TagType {
	@EnumValue(sn = 1)
	public static final EnumInfo SYSTEM = new EnumInfo("系统标签", "1", "系统标签");
	@EnumValue(sn = 2)
	public static final EnumInfo CUSTOM = new EnumInfo("用户自定义标签", "2", "用户自定义标签");
}
