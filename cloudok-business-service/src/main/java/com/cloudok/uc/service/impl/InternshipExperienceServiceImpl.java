package com.cloudok.uc.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cloudok.base.service.CompanyService;
import com.cloudok.base.service.IndustryService;
import com.cloudok.base.service.JobService;
import com.cloudok.base.vo.CompanyVO;
import com.cloudok.base.vo.IndustryVO;
import com.cloudok.base.vo.JobVO;
import com.cloudok.core.exception.CoreExceptionMessage;
import com.cloudok.core.exception.SystemException;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.service.AbstractService;
import com.cloudok.security.SecurityContextHelper;
import com.cloudok.uc.mapper.InternshipExperienceMapper;
import com.cloudok.uc.mapping.InternshipExperienceMapping;
import com.cloudok.uc.po.InternshipExperiencePO;
import com.cloudok.uc.service.InternshipExperienceService;
import com.cloudok.uc.vo.InternshipExperienceVO;

@Service
public class InternshipExperienceServiceImpl extends AbstractService<InternshipExperienceVO, InternshipExperiencePO>
		implements InternshipExperienceService {

	@Autowired
	private CompanyService companyService;

	@Autowired
	private JobService jobService;
	
	@Autowired
	private IndustryService industryService;

	@Autowired
	public InternshipExperienceServiceImpl(InternshipExperienceMapper repository) {
		super(repository);
	}

	@Override
	public InternshipExperienceVO create(InternshipExperienceVO d) {
		d.setMemberId(SecurityContextHelper.getCurrentUserId());
		d.setCompany(companyService.createOrGet(d.getCompany().getName()));
		d.setJob(jobService.createOrGet(d.getJob().getName()));
		return super.create(d);
	}

	@Override
	public InternshipExperienceVO update(InternshipExperienceVO d) {
		InternshipExperienceVO vo = this.get(d.getId());
		if (vo != null) {
			if (!vo.getMemberId().equals(SecurityContextHelper.getCurrentUserId())) {
				throw new SystemException(CoreExceptionMessage.NO_PERMISSION);
			}
		}
		d.setMemberId(SecurityContextHelper.getCurrentUserId());
		d.setCompany(companyService.createOrGet(d.getCompany().getName()));
		d.setJob(jobService.createOrGet(d.getJob().getName()));
		return super.update(d);
	}
	

	@Override
	public InternshipExperiencePO convert2PO(InternshipExperienceVO d) {
		InternshipExperiencePO po = super.convert2PO(d);
		po.setCompanyId(d.getCompany().getId());
		po.setJobId(d.getJob().getId());
		po.setIndustryId(d.getIndustry().getId());
		return po;
	}

	@Override
	public InternshipExperienceVO convert2VO(InternshipExperiencePO e) {
		InternshipExperienceVO vo = super.convert2VO(e);
		vo.setCompany(new CompanyVO(e.getCompanyId()));
		vo.setIndustry(new IndustryVO(e.getIndustryId()));
		vo.setJob(new JobVO(e.getJobId()));
		return vo;
	}

	@Override
	public List<InternshipExperienceVO> convert2VO(List<InternshipExperiencePO> e) {
		List<InternshipExperienceVO> list = super.convert2VO(e);
		if (!CollectionUtils.isEmpty(list)) {
			List<IndustryVO> industryList = industryService.get(list.stream().map(item -> item.getIndustry().getId()).distinct().collect(Collectors.toList()));
			List<JobVO> jobList = jobService.get(list.stream().map(item -> item.getJob().getId()).distinct().collect(Collectors.toList()));
			List<CompanyVO> companyList = companyService.get(list.stream().map(item -> item.getCompany().getId()).distinct().collect(Collectors.toList()));
			
			list.stream().forEach(item -> {
				companyList.stream().filter(i -> i.getId().equals(item.getCompany().getId())).findAny().ifPresent(i -> {
					item.setCompany(i);
				});
				jobList.stream().filter(i -> i.getId().equals(item.getJob().getId())).findAny().ifPresent(i -> {
					item.setJob(i);
				});
				industryList.stream().filter(i -> i.getId().equals(item.getIndustry().getId())).findAny().ifPresent(i -> {
					item.setIndustry(i);
				});
			});
		}

		return list;
	}

	@Override
	public Integer remove(Long pk) {
		InternshipExperienceVO vo = this.get(pk);
		if (vo != null) {
			if (!vo.getMemberId().equals(SecurityContextHelper.getCurrentUserId())) {
				throw new SystemException(CoreExceptionMessage.NO_PERMISSION);
			}
		}
		return super.remove(pk);
	}

	@Override
	public List<InternshipExperienceVO> getByMember(Long currentUserId) {
		return this.list(QueryBuilder.create(InternshipExperienceMapping.class)
				.and(InternshipExperienceMapping.MEMBERID, SecurityContextHelper.getCurrentUserId()).end());
	}

	@Override
	public InternshipExperienceVO getByMember(Long currentUserId, Long id) {
		return this.list(QueryBuilder.create(InternshipExperienceMapping.class)
				.and(InternshipExperienceMapping.MEMBERID, SecurityContextHelper.getCurrentUserId()).and(InternshipExperienceMapping.ID, id).end()).get(0);
	}
}
