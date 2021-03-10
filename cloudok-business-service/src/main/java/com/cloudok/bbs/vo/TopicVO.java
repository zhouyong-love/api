package com.cloudok.bbs.vo;

import com.cloudok.base.dict.enums.EnumInfo;

import lombok.Data;

@Data
public class TopicVO {
	private Long id;
	// type目前支持 0 系统推荐 1 研究领域 2 行业 3 社团 4 个性 5 状态标签 6 学校 7 专业
	private String type;
	private String name;
	private String icon;
	private Integer sn;

	public TopicVO() {

	}

	public TopicVO(Long id, EnumInfo type, String name, String icon, Integer sn) {
		super();
		this.id = id;
		this.type = type.getValue();
		this.name = name;
		this.icon = icon;
		this.sn = sn;
		if(sn == null) {
			sn = 99999;
		}
	}

	public TopicVO(Long id, EnumInfo type, String name, Integer sn) {
		super();
		this.id = id;
		this.type = type.getValue();
		this.name = name;
		this.sn = sn;
		if(sn == null) {
			sn = 99999;
		}
	}
 

}
