package com.cloudok.uc.service.impl;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cloudok.core.context.SpringApplicationContext;
import com.cloudok.core.exception.CoreExceptionMessage;
import com.cloudok.core.exception.SystemException;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.service.AbstractService;
import com.cloudok.enums.MemberProfileType;
import com.cloudok.exception.CloudOKExceptionMessage;
import com.cloudok.security.SecurityContextHelper;
import com.cloudok.uc.event.MemberProfileEvent;
import com.cloudok.uc.mapper.ProjectExperienceMapper;
import com.cloudok.uc.mapping.ProjectExperienceMapping;
import com.cloudok.uc.po.ProjectExperiencePO;
import com.cloudok.uc.service.ProjectExperienceService;
import com.cloudok.uc.vo.ProjectExperienceVO;
import com.cloudok.uc.vo.SwitchSNRequest;

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
		if(d.getSn() == null || d.getSn() == 0) {
			List<ProjectExperienceVO> list = this.list(QueryBuilder.create(ProjectExperienceMapping.class)
					.and(ProjectExperienceMapping.MEMBERID, SecurityContextHelper.getCurrentUserId()).end());
			if(!CollectionUtils.isEmpty(list)) {
				d.setSn(list.stream().mapToInt(item -> item.getSn()).max().getAsInt()+1);
			}else {
				d.setSn(1);
			}
		}
		ProjectExperienceVO v =  super.create(d);
		SpringApplicationContext.publishEvent(MemberProfileEvent.create(getCurrentUserId(),MemberProfileType.project,d));
		return v;
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
		if(d.getSn() == null) {
			d.setSn(vo.getSn());
		}
		ProjectExperienceVO v =  super.update(d);
		SpringApplicationContext.publishEvent(MemberProfileEvent.update(getCurrentUserId(),MemberProfileType.project,d,vo));
		return v;
	}

	@Override
	public Integer remove(Long pk) {
		ProjectExperienceVO vo = this.get(pk);
		if (vo != null) {
			if (!vo.getMemberId().equals(SecurityContextHelper.getCurrentUserId())) {
				throw new SystemException(CoreExceptionMessage.NO_PERMISSION);
			}
		}else {
			return 0;
		}
		int r = super.remove(pk);
		SpringApplicationContext.publishEvent(MemberProfileEvent.delete(getCurrentUserId(),MemberProfileType.project,vo));
		return r;
	}
	 @Override
	public ProjectExperienceVO convert2VO(ProjectExperiencePO e) {
		 ProjectExperienceVO vo = super.convert2VO(e);
		 if(vo.getSn() == null) {
			 vo.setSn(0);
		 }
		 return vo;
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
	

	@Override
	public Object switchSN(@Valid SwitchSNRequest switchSNRequest) {
		if(switchSNRequest.getSourceId() == null || switchSNRequest.getTargetId() == null) {
			throw new SystemException("数据记录不存在",CloudOKExceptionMessage.DEFAULT_ERROR);
		}
		List<ProjectExperienceVO> list = this.get(Arrays.asList(switchSNRequest.getSourceId(),switchSNRequest.getTargetId()));
		if(CollectionUtils.isEmpty(list) || list.size() != 2) {
			throw new SystemException("数据记录不存在",CloudOKExceptionMessage.DEFAULT_ERROR);
		}
		ProjectExperienceVO source = list.get(0);
		ProjectExperienceVO target = list.get(1);
		int sourceSn = source.getSn();
		int targetSn = target.getSn();
		source.setSn(targetSn);
		target.setSn(sourceSn);
		
		this.merge(source);
		this.merge(target);
		
		return true;
	}
}
