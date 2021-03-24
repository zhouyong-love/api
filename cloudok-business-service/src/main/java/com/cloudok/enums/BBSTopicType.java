package com.cloudok.enums;

import com.cloudok.base.dict.enums.annotation.Enum;
import com.cloudok.base.dict.enums.EnumInfo;
import com.cloudok.base.dict.enums.annotation.EnumValue;

@Enum(type = "bbsTopicType", label = "动态话题类型", describe = "动态话题类型")
public class BBSTopicType {
	//type目前支持 0 系统推荐标签 1 研究领域 2 行业 3 社团 4 个性 5 状态标签 6 学校 7 专业

	@EnumValue(sn = 0)
	public static final EnumInfo systemSuggestTag = new EnumInfo("系统推荐标签", "0", "系统推荐标签");
	
	@EnumValue(sn = 1)
	public static final EnumInfo researchDomain = new EnumInfo("研究领域", "1", "研究领域");
	
	@EnumValue(sn = 1)
	public static final EnumInfo industry = new EnumInfo("行业", "2", "行业");
	
	@EnumValue(sn = 1)
	public static final EnumInfo projectType = new EnumInfo("社团", "3", "社团");
	
	@EnumValue(sn = 1)
	public static final EnumInfo personalityTag = new EnumInfo("个性标签", "4", "个性标签");
	
	@EnumValue(sn = 1)
	public static final EnumInfo statementTag = new EnumInfo("状态标签", "5", "状态标签");
	
	@EnumValue(sn = 6)
	public static final EnumInfo school = new EnumInfo("学校", "6", "学校");
	
	@EnumValue(sn = 7)
	public static final EnumInfo specialism = new EnumInfo("专业", "7", "专业");

}
 
