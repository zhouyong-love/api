package com.cloudok.enums;

import com.cloudok.base.dict.enums.annotation.Enum;
import com.cloudok.base.dict.enums.EnumInfo;
import com.cloudok.base.dict.enums.annotation.EnumValue;

@Enum(type = "thumbsUpType", label = "点赞业务类型", describe = "点赞业务类型")
public class ThumbsUpType {
	
	@EnumValue(sn = 1)
	public static final EnumInfo post = new EnumInfo("动态", "1", "动态");
	
	@EnumValue(sn = 2)
	public static final EnumInfo comment = new EnumInfo("评论", "2", "评论");
	
}
 
