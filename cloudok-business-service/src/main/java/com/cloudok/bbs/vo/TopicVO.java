package com.cloudok.bbs.vo;

import com.cloudok.core.vo.VO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TopicVO extends VO {

	private static final long serialVersionUID = 237228683044969700L;
	
	
	private String name;
	
	
	private Long icon;
	
	
	private Integer status;
	
	
	private Integer postCount;
	
	
	private Integer type;
	
	public TopicVO(Long id) {
		this.setId(id);
	}
	
}
