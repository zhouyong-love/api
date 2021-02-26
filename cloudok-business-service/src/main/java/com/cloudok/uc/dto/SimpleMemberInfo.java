package com.cloudok.uc.dto;

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
public class SimpleMemberInfo {
	private Long id;
	private String nickName;
	private Long avatar;
	private String avatarUrl;
	private String sex;
	private EducationExperienceVO education;
	
	public SimpleMemberInfo(Long id) {
		this.setId(id);
	}
	
	
}
