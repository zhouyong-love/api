package com.cloudok.uc.dto;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.cloudok.uc.vo.EducationExperienceVO;
import com.cloudok.uc.vo.InternshipExperienceVO;
import com.cloudok.uc.vo.LinkMemberVO;
import com.cloudok.uc.vo.MemberTagsVO;
import com.cloudok.uc.vo.ProjectExperienceVO;
import com.cloudok.uc.vo.ResearchExperienceVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 完整的member对象
 * 
 * @author zhangtiebin
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WholeMemberDTO extends LinkMemberVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5347431703517148719L;

	private List<EducationExperienceVO> educationList;

	private List<InternshipExperienceVO> internshipList;

	private List<ResearchExperienceVO> researchList;

	private List<ProjectExperienceVO> projectList;

	private List<MemberTagsVO> tagsList;
//
//	private List<Long> recognizedMemberList; // 认可的人脉
//
//	private List<Long> recognizedByList; // 被那些人认可
	 
	public RecognizedMemberDTO toRecognized() {
		RecognizedMemberDTO target = new RecognizedMemberDTO();
		BeanUtils.copyProperties(this, target);
		return target;
	}

	public StrangeMemberDTO toStrangeMember() {
		StrangeMemberDTO target = new StrangeMemberDTO();
		BeanUtils.copyProperties(this, target);
		return target;
	}


	public SimpleMemberInfo toSampleInfo() {
		SimpleMemberInfo target = new SimpleMemberInfo();
		BeanUtils.copyProperties(this, target);
		if(!CollectionUtils.isEmpty(educationList)) {
			target.setEducation(educationList.get(0));
		}
		return target;
	}

	
}
