package com.cloudok.base.dict.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhijian.xia@foxmail.com
 * @date 2020年6月29日 下午10:55:42
 */
@Getter @Setter
public class EnumInfo{
	
	private String value;
	
	private String label;
	
	private String describe;
	
	
	public EnumInfo(String label,String value,String describe)
	{
		this.value=value;
		this.label=label;
		this.describe=describe;
	}
	
	public EnumInfo(String label,String value)
	{
		this.value=value;
		this.label=label;
	}
}