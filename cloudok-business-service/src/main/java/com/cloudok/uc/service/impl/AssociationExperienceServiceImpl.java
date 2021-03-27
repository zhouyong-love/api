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
import com.cloudok.uc.mapper.AssociationExperienceMapper;
import com.cloudok.uc.mapping.AssociationExperienceMapping;
import com.cloudok.uc.po.AssociationExperiencePO;
import com.cloudok.uc.service.AssociationExperienceService;
import com.cloudok.uc.vo.AssociationExperienceVO;
import com.cloudok.uc.vo.SwitchSNRequest;

@Service
public class AssociationExperienceServiceImpl extends AbstractService<AssociationExperienceVO, AssociationExperiencePO> implements AssociationExperienceService{

	@Autowired
	public AssociationExperienceServiceImpl(AssociationExperienceMapper repository) {
		super(repository);
	}
	
	@Override
	public AssociationExperienceVO create(AssociationExperienceVO d) {
		d.setMemberId(SecurityContextHelper.getCurrentUserId());
		if(d.getSn() == null || d.getSn() == 0) {
			List<AssociationExperienceVO> list = this.list(QueryBuilder.create(AssociationExperienceMapping.class).and(AssociationExperienceMapping.MEMBERID, SecurityContextHelper.getCurrentUserId()).end());
			if(!CollectionUtils.isEmpty(list)) {
				d.setSn(list.stream().mapToInt(item -> item.getSn()).max().getAsInt()+1);
			}else {
				d.setSn(1);
			}
		}
		AssociationExperienceVO v = super.create(d);
		SpringApplicationContext.publishEvent(MemberProfileEvent.create(getCurrentUserId(),MemberProfileType.association,d));
		return v;
	}
	@Override
	public AssociationExperienceVO convert2VO(AssociationExperiencePO e) {
		AssociationExperienceVO v =  super.convert2VO(e);
		if(v.getSn() == null) {
			v.setSn(0);
		}
		return v;
	}
	@Override
	public AssociationExperienceVO update(AssociationExperienceVO d) {
		AssociationExperienceVO vo = this.get(d.getId());
		if (vo != null) {
			if (!vo.getMemberId().equals(SecurityContextHelper.getCurrentUserId())) {
				throw new SystemException(CoreExceptionMessage.NO_PERMISSION);
			}
		}
		d.setMemberId(SecurityContextHelper.getCurrentUserId());
		SpringApplicationContext.publishEvent(MemberProfileEvent.update(getCurrentUserId(),MemberProfileType.association,d,vo));
		if(d.getSn() == null) {
			d.setSn(vo.getSn());
		}
		AssociationExperienceVO t = super.update(d);
		return t;
	}
	@Override
	public Integer remove(Long pk) {
		AssociationExperienceVO vo = this.get(pk);
		if (vo != null) {
			if (!vo.getMemberId().equals(SecurityContextHelper.getCurrentUserId())) {
				throw new SystemException(CoreExceptionMessage.NO_PERMISSION);
			}
		}else {
			return 0;
		}
		int r =  super.remove(pk);
		SpringApplicationContext.publishEvent(MemberProfileEvent.delete(getCurrentUserId(),MemberProfileType.association,vo));
		return r;
	}

	@Override
	public Object switchSN(@Valid SwitchSNRequest switchSNRequest) {
		if(switchSNRequest.getSourceId() == null || switchSNRequest.getTargetId() == null) {
			throw new SystemException("数据记录不存在",CloudOKExceptionMessage.DEFAULT_ERROR);
		}
		List<AssociationExperienceVO> list = this.get(Arrays.asList(switchSNRequest.getSourceId(),switchSNRequest.getTargetId()));
		if(CollectionUtils.isEmpty(list) || list.size() != 2) {
			throw new SystemException("数据记录不存在",CloudOKExceptionMessage.DEFAULT_ERROR);
		}
		AssociationExperienceVO source = list.get(0);
		AssociationExperienceVO target = list.get(1);
		int sourceSn = source.getSn();
		int targetSn = target.getSn();
		source.setSn(targetSn);
		target.setSn(sourceSn);
		
		this.merge(source);
		this.merge(target);
		
		return true;
	}
}
