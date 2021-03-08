package com.cloudok.enums;

import com.cloudok.base.dict.enums.annotation.Enum;
import com.cloudok.base.dict.enums.EnumInfo;
import com.cloudok.base.dict.enums.annotation.EnumValue;

@Enum(type = "tagCategory", label = "标签分类", describe = "标签分类")
public class TagCategory {
	
	@EnumValue(sn = 1)
	public static final EnumInfo personality = new EnumInfo("个性标签", "personality", "个性标签");
	
	@EnumValue(sn = 2)
	public static final EnumInfo statement = new EnumInfo("状态标签", "statement", "状态标签");
	
	@EnumValue(sn = 5)
	public static final EnumInfo systemTopic = new EnumInfo("动态系统推荐标签", "topic", "动态系统推荐标签");
}
