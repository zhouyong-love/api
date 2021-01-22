package com.cloudok.base.dict.enums;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cloudok.base.dict.enums.annotation.Enum;
import com.cloudok.base.dict.enums.annotation.EnumValue;
import com.cloudok.base.dict.enums.vo.EnumVO;
import com.cloudok.base.dict.enums.vo.EnumValueVO;

/**
 * 
 * @author xzj
 *
 */
public class EnumHandler {

	private static Map<String, EnumVO> enums=new HashMap<>();
	
	public static void addEnum(Enum e,List<EnumInfo> enumInfos,List<EnumValue> enumValues) {
		EnumVO enumVO=new EnumVO();
		enumVO.setType(e.type());
		enumVO.setDescrib(e.describe());
		enumVO.setLabel(e.label());
		List<EnumValueVO> values=new ArrayList<>();
		for (int i = 0; i < enumInfos.size(); i++) {
			EnumValueVO enumValueVO=new EnumValueVO();
			enumValueVO.setDescribe(enumInfos.get(i).getDescribe());
			enumValueVO.setLabel(enumInfos.get(i).getLabel());
			enumValueVO.setValue(enumInfos.get(i).getValue());
			enumValueVO.setSn(enumValues.get(i).sn());
			values.add(enumValueVO);
		}
		enumVO.setValues(values);
		enums.put(e.type(), enumVO);
	}
	
	public static Map<String, EnumVO> getEnumMap(){
		return enums;
	}
	

	public static EnumVO getEnum(String type){
		return enums.get(type);
	}
	
	public static Collection<EnumVO> getEnums(){
		return enums.values();
	}
}
