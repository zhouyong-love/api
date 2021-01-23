package com.cloudok.enums;

import com.cloudok.base.dict.enums.annotation.Enum;
import com.cloudok.base.dict.enums.EnumInfo;
import com.cloudok.base.dict.enums.annotation.EnumValue;

@Enum(type = "tagCategory", label = "标签分类", describe = "标签分类")
public class TagCategory {
	
	@EnumValue(sn = 1)
	public static final EnumInfo hobby = new EnumInfo("爱好标签", "1", "爱好标签");
	
	@EnumValue(sn = 2)
	public static final EnumInfo status = new EnumInfo("状态", "2", "状态");

	@EnumValue(sn = 3)
	public static final EnumInfo study = new EnumInfo("学习标签", "3", "学习标签");

	@EnumValue(sn = 4)
	public static final EnumInfo plan = new EnumInfo("规划标签", "4", "规划标签");
}
