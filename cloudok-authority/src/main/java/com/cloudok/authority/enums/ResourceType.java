package com.cloudok.authority.enums;

import com.cloudok.base.dict.enums.annotation.Enum;
import com.cloudok.base.dict.enums.EnumInfo;
import com.cloudok.base.dict.enums.annotation.EnumValue;

/**
 * @author zhijian.xia@foxmail.com
 */
@Enum(type = "res_type",label = "Resource type")
public class ResourceType {

	@EnumValue(sn = 0)
	public static final EnumInfo MENU=new EnumInfo("menu", "0");
	
	@EnumValue(sn = 1)
	public static final EnumInfo INTERFACE=new EnumInfo("interface", "1");
	
	@EnumValue(sn = 2)
	public static final EnumInfo BUTTON=new EnumInfo("button", "2");
	
	@EnumValue(sn = 3)
	public static final EnumInfo LINK=new EnumInfo("link", "3");
	
	public static EnumInfo parse(String value) {
		switch (value) {
		case "0": {
			return MENU;
		}
		case "1": {
			return INTERFACE;
		}
		case "2": {
			return BUTTON;
		}
		case "3": {
			return LINK;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + value);
		}
	}
	
}
