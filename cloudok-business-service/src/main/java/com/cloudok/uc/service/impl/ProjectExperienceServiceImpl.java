package com.cloudok.uc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudok.core.exception.CoreExceptionMessage;
import com.cloudok.core.exception.SystemException;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.service.AbstractService;
import com.cloudok.security.SecurityContextHelper;
import com.cloudok.uc.mapper.ProjectExperienceMapper;
import com.cloudok.uc.mapping.ProjectExperienceMapping;
import com.cloudok.uc.po.ProjectExperiencePO;
import com.cloudok.uc.service.ProjectExperienceService;
import com.cloudok.uc.vo.ProjectExperienceVO;

@Service
public class ProjectExperienceServiceImpl extends AbstractService<ProjectExperienceVO, ProjectExperiencePO>
		implements ProjectExperienceService {

	@Autowired
	public ProjectExperienceServiceImpl(ProjectExperienceMapper repository) {
		super(repository);
	}

	@Override
	public ProjectExperienceVO create(ProjectExperienceVO d) {
		d.setMemberId(SecurityContextHelper.getCurrentUserId());
		return super.create(d);
	}

	@Override
	public ProjectExperienceVO update(ProjectExperienceVO d) {
		ProjectExperienceVO vo = this.get(d.getId());
		if (vo != null) {
			if (!vo.getMemberId().equals(SecurityContextHelper.getCurrentUserId())) {
				throw new SystemException(CoreExceptionMessage.NO_PERMISSION);
			}
		}
		d.setMemberId(SecurityContextHelper.getCurrentUserId());
		return super.update(d);
	}

	@Override
	public Integer remove(Long pk) {
		ProjectExperienceVO vo = this.get(pk);
		if (vo != null) {
			if (!vo.getMemberId().equals(SecurityContextHelper.getCurrentUserId())) {
				throw new SystemException(CoreExceptionMessage.NO_PERMISSION);
			}
		}
		return super.remove(pk);
	}

	@Override
	public List<ProjectExperienceVO> getByMember(Long currentUserId) {
		return this.list(QueryBuilder.create(ProjectExperienceMapping.class)
				.and(ProjectExperienceMapping.MEMBERID, SecurityContextHelper.getCurrentUserId()).end());
	}

	@Override
	public ProjectExperienceVO getByMember(Long currentUserId, Long id) {
		return this.list(QueryBuilder.create(ProjectExperienceMapping.class)
				.and(ProjectExperienceMapping.MEMBERID, SecurityContextHelper.getCurrentUserId()).and(ProjectExperienceMapping.ID, id).end()).get(0);
	}
}
