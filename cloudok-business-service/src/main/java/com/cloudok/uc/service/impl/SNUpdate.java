package com.cloudok.uc.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cloudok.core.query.QueryBuilder;
import com.cloudok.uc.mapping.EducationExperienceMapping;
import com.cloudok.uc.mapping.InternshipExperienceMapping;
import com.cloudok.uc.mapping.MemberTagsMapping;
import com.cloudok.uc.mapping.ProjectExperienceMapping;
import com.cloudok.uc.mapping.ResearchExperienceMapping;
import com.cloudok.uc.service.EducationExperienceService;
import com.cloudok.uc.service.InternshipExperienceService;
import com.cloudok.uc.service.MemberTagsService;
import com.cloudok.uc.service.ProjectExperienceService;
import com.cloudok.uc.service.ResearchExperienceService;
import com.cloudok.uc.vo.EducationExperienceVO;
import com.cloudok.uc.vo.InternshipExperienceVO;
import com.cloudok.uc.vo.MemberTagsVO;
import com.cloudok.uc.vo.ProjectExperienceVO;
import com.cloudok.uc.vo.ResearchExperienceVO;

@Service
public class SNUpdate implements InitializingBean{
	@Autowired
	private EducationExperienceService educationExperienceService; 
	
	@Autowired
	private InternshipExperienceService internshipExperienceService; 
	
	@Autowired
	private ProjectExperienceService projectExperienceService; 
	
	@Autowired
	private ResearchExperienceService researchExperienceService; 
	
	@Autowired
	private MemberTagsService memberTagsService;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		new Thread(()->{
			try {
				Thread.sleep(TimeUnit.MINUTES.toMillis(1));
			} catch (InterruptedException e) {
			}
			this.updateSN();
		}) .start();
		
	}

	private void updateSN() {
		this.updateEducationExperience();
		this.updateInternshipExperience();
		this.updateProjectExperience();
		this.updateResearchExperience();
		this.updateMemberTags();
	}
	private void updateEducationExperience() {
		List<EducationExperienceVO> list = educationExperienceService.list(QueryBuilder.create(EducationExperienceMapping.class)
				.and(EducationExperienceMapping.SN, 0).end()
				.disenablePaging());
		if(!CollectionUtils.isEmpty(list)) {
			Map<Long,List<EducationExperienceVO>> map = list.stream().collect(Collectors.groupingBy(EducationExperienceVO::getMemberId));
			map.forEach((key,value)->{
				List<EducationExperienceVO> memberList = educationExperienceService.list(QueryBuilder.create(EducationExperienceMapping.class)
						.and(EducationExperienceMapping.MEMBERID, key).end().sort(EducationExperienceMapping.GRADE).desc());
				int lastSN  = memberList.stream().mapToInt(item -> item.getSn()).max().getAsInt();
				for(int i = 0;i<memberList.size();i++) {
					EducationExperienceVO  m = memberList.get(i);
					if(m.getSn() == 0) {
						m.setSn(lastSN+i+1);
					}else {
						continue;
					}
					this.educationExperienceService.merge(m);
				}
				
			});
		}
	}
	

	private void updateInternshipExperience() {
		List<InternshipExperienceVO> list = internshipExperienceService.list(QueryBuilder.create(InternshipExperienceMapping.class)
				.and(InternshipExperienceMapping.SN, 0).end()
				.disenablePaging());
		if(!CollectionUtils.isEmpty(list)) {
			Map<Long,List<InternshipExperienceVO>> map = list.stream().collect(Collectors.groupingBy(InternshipExperienceVO::getMemberId));
			map.forEach((key,value)->{
				List<InternshipExperienceVO> memberList = internshipExperienceService.list(QueryBuilder.create(InternshipExperienceMapping.class)
						.and(InternshipExperienceMapping.MEMBERID, key).end().sort(InternshipExperienceMapping.ID).asc());
				int lastSN  = memberList.stream().mapToInt(item -> item.getSn()).max().getAsInt();
				for(int i = 0;i<memberList.size();i++) {
					InternshipExperienceVO  m = memberList.get(i);
					if(m.getSn() == 0) {
						m.setSn(lastSN+i+1);
					}else {
						continue;
					}
					this.internshipExperienceService.merge(m);
				}
				
			});
		}
	}
	

	private void updateProjectExperience() {
		List<ProjectExperienceVO> list = projectExperienceService.list(QueryBuilder.create(ProjectExperienceMapping.class)
				.and(ProjectExperienceMapping.SN, 0).end()
				.disenablePaging());
		if(!CollectionUtils.isEmpty(list)) {
			Map<Long,List<ProjectExperienceVO>> map = list.stream().collect(Collectors.groupingBy(ProjectExperienceVO::getMemberId));
			map.forEach((key,value)->{
				List<ProjectExperienceVO> memberList = projectExperienceService.list(QueryBuilder.create(ProjectExperienceMapping.class)
						.and(ProjectExperienceMapping.MEMBERID, key).end().sort(ProjectExperienceMapping.ID).asc());
				int lastSN  = memberList.stream().mapToInt(item -> item.getSn()).max().getAsInt();
				for(int i = 0;i<memberList.size();i++) {
					ProjectExperienceVO m = memberList.get(i);
					if(m.getSn() == 0) {
						m.setSn(lastSN+i+1);
					}else {
						continue;
					}
					this.projectExperienceService.merge(m);
				}
				
			});
		}
	}
	

	private void updateResearchExperience() {
		List<ResearchExperienceVO> list = researchExperienceService.list(QueryBuilder.create(ResearchExperienceMapping.class)
				.and(ProjectExperienceMapping.SN, 0).end()
				.disenablePaging());
		if(!CollectionUtils.isEmpty(list)) {
			Map<Long,List<ResearchExperienceVO>> map = list.stream().collect(Collectors.groupingBy(ResearchExperienceVO::getMemberId));
			map.forEach((key,value)->{
				List<ResearchExperienceVO> memberList = researchExperienceService.list(QueryBuilder.create(ResearchExperienceMapping.class)
						.and(ResearchExperienceMapping.MEMBERID, key).end().sort(ResearchExperienceMapping.ID).asc());
				int lastSN  = memberList.stream().mapToInt(item -> item.getSn()).max().getAsInt();
				for(int i = 0;i<memberList.size();i++) {
					ResearchExperienceVO m = memberList.get(i);
					if(m.getSn() == 0) {
						m.setSn(lastSN+i+1);
					}else {
						continue;
					}
					this.researchExperienceService.merge(m);
				}
			});
		}
	}
	
	private void updateMemberTags() {
		List<MemberTagsVO> list = memberTagsService.list(QueryBuilder.create(MemberTagsMapping.class)
				.and(MemberTagsMapping.SN, 0).end()
				.disenablePaging());
		if(!CollectionUtils.isEmpty(list)) {
			Map<Long,List<MemberTagsVO>> map = list.stream().collect(Collectors.groupingBy(MemberTagsVO::getMemberId));
			map.forEach((key,value)->{
				List<MemberTagsVO> memberList = memberTagsService.list(QueryBuilder.create(MemberTagsMapping.class)
						.and(MemberTagsMapping.MEMBERID, key).end().sort(MemberTagsMapping.ID).asc());
				int lastSN  = memberList.stream().mapToInt(item -> item.getSn()).max().getAsInt();
				for(int i = 0;i<memberList.size();i++) {
					MemberTagsVO m = memberList.get(i);
					if(m.getSn() == 0) {
						m.setSn(lastSN+i+1);
					}else {
						continue;
					}
					this.memberTagsService.merge(m);
				}
			});
		}
	}
	
}
