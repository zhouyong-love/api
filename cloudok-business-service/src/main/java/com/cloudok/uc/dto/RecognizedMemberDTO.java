package com.cloudok.uc.dto;

import java.util.List;

import com.cloudok.uc.vo.EducationExperienceVO;
import com.cloudok.uc.vo.InternshipExperienceVO;
import com.cloudok.uc.vo.MemberTagsVO;
import com.cloudok.uc.vo.MemberVO;
import com.cloudok.uc.vo.ProjectExperienceVO;
import com.cloudok.uc.vo.ResearchExperienceVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 认可的人看的member对象
 * 
 * @author zhangtiebin
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecognizedMemberDTO extends MemberVO {

	private static final long serialVersionUID = 212382535356088200L;
	private List<EducationExperienceVO> educationList;

	private List<InternshipExperienceVO> internshipList;

	private List<ResearchExperienceVO> researchList;

	private List<ProjectExperienceVO> projectList;

	private List<MemberTagsVO> tagsList;

}
