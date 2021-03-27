package com.cloudok.uc.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cloudok.base.service.SchoolService;
import com.cloudok.base.service.SpecialismService;
import com.cloudok.base.vo.SchoolVO;
import com.cloudok.base.vo.SpecialismVO;
import com.cloudok.core.context.SpringApplicationContext;
import com.cloudok.core.exception.CoreExceptionMessage;
import com.cloudok.core.exception.SystemException;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.service.AbstractService;
import com.cloudok.enums.MemberProfileType;
import com.cloudok.exception.CloudOKExceptionMessage;
import com.cloudok.security.SecurityContextHelper;
import com.cloudok.uc.event.MemberProfileEvent;
import com.cloudok.uc.mapper.EducationExperienceMapper;
import com.cloudok.uc.mapping.EducationExperienceMapping;
import com.cloudok.uc.po.EducationExperiencePO;
import com.cloudok.uc.service.EducationExperienceService;
import com.cloudok.uc.service.MemberService;
import com.cloudok.uc.vo.EducationExperienceVO;
import com.cloudok.uc.vo.MemberVO;
import com.cloudok.uc.vo.SwitchSNRequest;

@Service
public class EducationExperienceServiceImpl extends AbstractService<EducationExperienceVO, EducationExperiencePO>
		implements EducationExperienceService {
	@Autowired
	private SchoolService schoolService;
	@Autowired
	private SpecialismService specialismService;

	@Autowired
	private MemberService memberService;
	
	@Autowired
	public EducationExperienceServiceImpl(EducationExperienceMapper repository) {
		super(repository);
	}

	@Override
	public EducationExperienceVO create(EducationExperienceVO d) {
		d.setMemberId(SecurityContextHelper.getCurrentUserId());
		MemberVO member = new MemberVO();
		member.setState(memberService.get(d.getMemberId()).getState());
		member.getState().setFillEduInfo(true);
		member.setId(d.getMemberId());
		memberService.merge(member);
		
		if(d.getSn() == null || d.getSn() == 0) {
			List<EducationExperienceVO> list = this.list(QueryBuilder.create(EducationExperienceMapping.class)
					.and(EducationExperienceMapping.MEMBERID, SecurityContextHelper.getCurrentUserId()).end());
			if(!CollectionUtils.isEmpty(list)) {
				d.setSn(list.stream().mapToInt(item -> item.getSn()).max().getAsInt()+1);
			}else {
				d.setSn(1);
			}
		}
		EducationExperienceVO vo = super.create(d);
		SpringApplicationContext.publishEvent(MemberProfileEvent.create(getCurrentUserId(),MemberProfileType.eduction,d));
		
		return vo;
	}

	@Override
	public EducationExperienceVO update(EducationExperienceVO d) {
		EducationExperienceVO vo = this.get(d.getId());
		if (vo != null) {
			if (!vo.getMemberId().equals(SecurityContextHelper.getCurrentUserId())) {
				throw new SystemException(CoreExceptionMessage.NO_PERMISSION);
			}
		}
		d.setMemberId(SecurityContextHelper.getCurrentUserId());
		if(d.getSn() == null) {
			d.setSn(vo.getSn());
		}
		EducationExperienceVO t = super.update(d);
		SpringApplicationContext.publishEvent(MemberProfileEvent.update(getCurrentUserId(),MemberProfileType.eduction,d,vo));
		return t;
	}

	@Override
	public EducationExperiencePO convert2PO(EducationExperienceVO d) {
		EducationExperiencePO po = super.convert2PO(d);
		po.setSchoolId(d.getSchool().getId());
		po.setSpecialismId(d.getSpecialism().getId());
		return po;
	}

	@Override
	public EducationExperienceVO convert2VO(EducationExperiencePO e) {
		EducationExperienceVO vo = super.convert2VO(e);
		if(vo.getSn() == null) {
			vo.setSn(0);
		}
		vo.setSchool(new SchoolVO(e.getSchoolId()));
		vo.setSpecialism(new SpecialismVO(e.getSpecialismId()));
		return vo;
	}

	@Override
	public List<EducationExperienceVO> convert2VO(List<EducationExperiencePO> e) {
		List<EducationExperienceVO> list = super.convert2VO(e);
		if (!CollectionUtils.isEmpty(list)) {
			List<SchoolVO> schoolList = schoolService.get(list.stream().map(item -> item.getSchool().getId()).distinct().collect(Collectors.toList()));
			List<SpecialismVO> specialList = specialismService.get(list.stream().map(item -> item.getSpecialism().getId()).distinct().collect(Collectors.toList()));
			
			list.stream().forEach(item -> {
				schoolList.stream().filter(i -> i.getId().equals(item.getSchool().getId())).findAny().ifPresent(i -> {
					item.setSchool(i);
				});
				specialList.stream().filter(i -> i.getId().equals(item.getSpecialism().getId())).findAny().ifPresent(i -> {
					item.setSpecialism(i);
				});
			});
		}

		return list;
	}

	@Override
	public Integer remove(Long pk) {
		EducationExperienceVO vo = this.get(pk);
		if (vo != null) {
			if (!vo.getMemberId().equals(SecurityContextHelper.getCurrentUserId())) {
				throw new SystemException(CoreExceptionMessage.NO_PERMISSION);
			}
		}else {
			return 0;
		}
		int r =  super.remove(pk);
		SpringApplicationContext.publishEvent(MemberProfileEvent.delete(getCurrentUserId(),MemberProfileType.eduction,vo));
		return r;
	}

	@Override
	public List<EducationExperienceVO> getByMember(Long currentUserId) {
		return this.list(QueryBuilder.create(EducationExperienceMapping.class)
				.and(EducationExperienceMapping.MEMBERID, SecurityContextHelper.getCurrentUserId()).end().sort(EducationExperienceMapping.SN).asc());
	}

	@Override
	public EducationExperienceVO getByMember(Long currentUserId, Long educationId) {
		return this.list(QueryBuilder.create(EducationExperienceMapping.class)
				.and(EducationExperienceMapping.MEMBERID, SecurityContextHelper.getCurrentUserId()).and(EducationExperienceMapping.ID, educationId).end()).get(0);
	}
	
	@Override
	public Object switchSN(@Valid SwitchSNRequest switchSNRequest) {
		if(switchSNRequest.getSourceId() == null || switchSNRequest.getTargetId() == null) {
			throw new SystemException("数据记录不存在",CloudOKExceptionMessage.DEFAULT_ERROR);
		}
		List<EducationExperienceVO> list = this.get(Arrays.asList(switchSNRequest.getSourceId(),switchSNRequest.getTargetId()));
		if(CollectionUtils.isEmpty(list) || list.size() != 2) {
			throw new SystemException("数据记录不存在",CloudOKExceptionMessage.DEFAULT_ERROR);
		}
		EducationExperienceVO source = list.get(0);
		EducationExperienceVO target = list.get(1);
		int sourceSn = source.getSn();
		int targetSn = target.getSn();
		source.setSn(targetSn);
		target.setSn(sourceSn);
		
		this.merge(source);
		this.merge(target);
		
		return true;
	}
}
