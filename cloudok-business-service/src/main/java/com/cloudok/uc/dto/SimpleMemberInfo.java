package com.cloudok.uc.dto;

import com.cloudok.core.vo.VO;
import com.cloudok.uc.vo.EducationExperienceVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimpleMemberInfo extends VO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3626730213528072759L;
	private Long id;
	private String nickName;
	private Long avatar;
	private String avatarUrl;
	private String sex;
	private EducationExperienceVO education;
	private boolean from;
	private boolean to;
	
	public SimpleMemberInfo(Long id) {
		this.setId(id);
	}
	
	
}
