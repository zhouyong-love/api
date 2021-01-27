package com.cloudok.uc.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cloudok.base.service.SchoolService;
import com.cloudok.base.service.SpecialismService;
import com.cloudok.base.vo.SchoolVO;
import com.cloudok.base.vo.SpecialismVO;
import com.cloudok.core.exception.CoreExceptionMessage;
import com.cloudok.core.exception.SystemException;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.service.AbstractService;
import com.cloudok.security.SecurityContextHelper;
import com.cloudok.uc.mapper.EducationExperienceMapper;
import com.cloudok.uc.mapping.EducationExperienceMapping;
import com.cloudok.uc.po.EducationExperiencePO;
import com.cloudok.uc.service.EducationExperienceService;
import com.cloudok.uc.vo.EducationExperienceVO;

@Service
public class EducationExperienceServiceImpl extends AbstractService<EducationExperienceVO, EducationExperiencePO>
		implements EducationExperienceService {
	@Autowired
	private SchoolService schoolService;
	@Autowired
	private SpecialismService specialismService;

	@Autowired
	public EducationExperienceServiceImpl(EducationExperienceMapper repository) {
		super(repository);
	}

	@Override
	public EducationExperienceVO create(EducationExperienceVO d) {
		d.setMemberId(SecurityContextHelper.getCurrentUserId());
		return super.create(d);
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
		return super.update(d);
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
		}
		return super.remove(pk);
	}

	@Override
	public List<EducationExperienceVO> getByMember(Long currentUserId) {
		return this.list(QueryBuilder.create(EducationExperienceMapping.class)
				.and(EducationExperienceMapping.MEMBERID, SecurityContextHelper.getCurrentUserId()).end());
	}

	@Override
	public EducationExperienceVO getByMember(Long currentUserId, Long educationId) {
		return this.list(QueryBuilder.create(EducationExperienceMapping.class)
				.and(EducationExperienceMapping.MEMBERID, SecurityContextHelper.getCurrentUserId()).and(EducationExperienceMapping.ID, educationId).end()).get(0);
	}
}
