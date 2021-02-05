package com.cloudok.uc.dto;

import java.io.Serializable;

import com.cloudok.uc.vo.EducationExperienceVO;
import com.cloudok.uc.vo.MemberVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimpleMemberDTO implements Serializable{

	private static final long serialVersionUID = 1303591408048659491L;

	private MemberVO member;
	
	private EducationExperienceVO eduExperience;
	
	private long friendCount;
	
	private long fromCount;
	
	private long toCount;
}
