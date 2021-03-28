package com.cloudok.uc.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cloudok.base.service.CompanyService;
import com.cloudok.base.service.IndustryService;
import com.cloudok.base.service.JobService;
import com.cloudok.base.vo.CompanyVO;
import com.cloudok.base.vo.IndustryVO;
import com.cloudok.base.vo.JobVO;
import com.cloudok.core.context.SpringApplicationContext;
import com.cloudok.core.exception.CoreExceptionMessage;
import com.cloudok.core.exception.SystemException;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.service.AbstractService;
import com.cloudok.enums.MemberProfileType;
import com.cloudok.exception.CloudOKExceptionMessage;
import com.cloudok.security.SecurityContextHelper;
import com.cloudok.uc.event.MemberProfileEvent;
import com.cloudok.uc.mapper.InternshipExperienceMapper;
import com.cloudok.uc.mapping.InternshipExperienceMapping;
import com.cloudok.uc.po.InternshipExperiencePO;
import com.cloudok.uc.service.InternshipExperienceService;
import com.cloudok.uc.vo.InternshipExperienceVO;
import com.cloudok.uc.vo.SwitchSNRequest;

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
		if(d.getSn() == null || d.getSn() == 0) {
			List<InternshipExperienceVO> list = this.list(QueryBuilder.create(InternshipExperienceMapping.class)
					.and(InternshipExperienceMapping.MEMBERID, SecurityContextHelper.getCurrentUserId()).end());
			if(!CollectionUtils.isEmpty(list)) {
				d.setSn(list.stream().mapToInt(item -> item.getSn()).max().getAsInt()+1);
			}else {
				d.setSn(1);
			}
		}
		d.setIndustry(this.industryService.get(d.getIndustry().getId()));
		InternshipExperienceVO v = super.create(d);
		SpringApplicationContext.publishEvent(MemberProfileEvent.create(getCurrentUserId(),MemberProfileType.internship,d));
		return v;
		
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
		if(d.getSn() == null) {
			d.setSn(vo.getSn());
		}
		d.setIndustry(this.industryService.get(d.getIndustry().getId()));
		InternshipExperienceVO v = super.update(d);
		SpringApplicationContext.publishEvent(MemberProfileEvent.update(getCurrentUserId(),MemberProfileType.internship,d,vo));
		return v;
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
		if(vo.getSn()==null) {
			vo.setSn(0);
		}
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
		}else {
			return 0;
		}
		int r =  super.remove(pk);
		SpringApplicationContext.publishEvent(MemberProfileEvent.delete(getCurrentUserId(),MemberProfileType.internship,vo));
		return r;
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
	
	@Override
	public Object switchSN(@Valid SwitchSNRequest switchSNRequest) {
		if(switchSNRequest.getSourceId() == null || switchSNRequest.getTargetId() == null) {
			throw new SystemException("数据记录不存在",CloudOKExceptionMessage.DEFAULT_ERROR);
		}
		List<InternshipExperienceVO> list = this.get(Arrays.asList(switchSNRequest.getSourceId(),switchSNRequest.getTargetId()));
		if(CollectionUtils.isEmpty(list) || list.size() != 2) {
			throw new SystemException("数据记录不存在",CloudOKExceptionMessage.DEFAULT_ERROR);
		}
		InternshipExperienceVO source = list.get(0);
		InternshipExperienceVO target = list.get(1);
		int sourceSn = source.getSn();
		int targetSn = target.getSn();
		source.setSn(targetSn);
		target.setSn(sourceSn);
		
		this.merge(source);
		this.merge(target);
		
		return true;
	}
}
