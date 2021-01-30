package com.cloudok.uc.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cloudok.base.service.ResearchDomainService;
import com.cloudok.base.vo.ResearchDomainVO;
import com.cloudok.core.context.SpringApplicationContext;
import com.cloudok.core.exception.CoreExceptionMessage;
import com.cloudok.core.exception.SystemException;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.service.AbstractService;
import com.cloudok.security.SecurityContextHelper;
import com.cloudok.uc.event.MemberUpdateEvent;
import com.cloudok.uc.mapper.ResearchExperienceMapper;
import com.cloudok.uc.mapping.ResearchExperienceMapping;
import com.cloudok.uc.po.ResearchExperiencePO;
import com.cloudok.uc.service.ResearchExperienceService;
import com.cloudok.uc.vo.MemberVO;
import com.cloudok.uc.vo.ResearchExperienceVO;

@Service
public class ResearchExperienceServiceImpl extends AbstractService<ResearchExperienceVO, ResearchExperiencePO> implements ResearchExperienceService{

	@Autowired
	private ResearchDomainService researchDomainService;
	
	@Autowired
	public ResearchExperienceServiceImpl(ResearchExperienceMapper repository) {
		super(repository);
	}

	@Override
	public ResearchExperienceVO create(ResearchExperienceVO d) {
		d.setDomain(researchDomainService.createOrGet(d.getDomain().getName()));
		d.setMemberId(SecurityContextHelper.getCurrentUserId());
		ResearchExperienceVO v =  super.create(d);
		SpringApplicationContext.publishEvent(new MemberUpdateEvent(new MemberVO(SecurityContextHelper.getCurrentUserId())));
		return v;
	}

	@Override
	public ResearchExperienceVO update(ResearchExperienceVO d) {
		ResearchExperienceVO vo = this.get(d.getId());
		if (vo != null) {
			if (!vo.getMemberId().equals(SecurityContextHelper.getCurrentUserId())) {
				throw new SystemException(CoreExceptionMessage.NO_PERMISSION);
			}
		}
		d.setMemberId(SecurityContextHelper.getCurrentUserId());
		d.setDomain(researchDomainService.createOrGet(d.getDomain().getName()));
		ResearchExperienceVO v =  super.update(d);
		SpringApplicationContext.publishEvent(new MemberUpdateEvent(new MemberVO(SecurityContextHelper.getCurrentUserId())));
		return v;
	}

	@Override
	public Integer remove(Long pk) {
		ResearchExperienceVO vo = this.get(pk);
		if (vo != null) {
			if (!vo.getMemberId().equals(SecurityContextHelper.getCurrentUserId())) {
				throw new SystemException(CoreExceptionMessage.NO_PERMISSION);
			}
		}
		int r =  super.remove(pk);
		SpringApplicationContext.publishEvent(new MemberUpdateEvent(new MemberVO(SecurityContextHelper.getCurrentUserId())));
		return r;
	}


	@Override
	public ResearchExperiencePO convert2PO(ResearchExperienceVO d) {
		ResearchExperiencePO po = super.convert2PO(d);
		po.setDomainId(d.getDomain().getId());
		return po;
	}

	@Override
	public ResearchExperienceVO convert2VO(ResearchExperiencePO e) {
		ResearchExperienceVO vo = super.convert2VO(e);
		vo.setDomain(new ResearchDomainVO(e.getDomainId()));
		return vo;
	}

	@Override
	public List<ResearchExperienceVO> convert2VO(List<ResearchExperiencePO> e) {
		List<ResearchExperienceVO> list = super.convert2VO(e);
		if (!CollectionUtils.isEmpty(list)) {
			List<ResearchDomainVO> domainList = researchDomainService.get(list.stream().map(item -> item.getDomain().getId()).distinct().collect(Collectors.toList()));
			list.stream().forEach(item -> {
				domainList.stream().filter(i -> i.getId().equals(item.getDomain().getId())).findAny().ifPresent(i -> {
					item.setDomain(i);
				}); 
			});
		}

		return list;
	}

	@Override
	public List<ResearchExperienceVO> getByMember(Long currentUserId) {
		return this.list(QueryBuilder.create(ResearchExperienceMapping.class)
				.and(ResearchExperienceMapping.MEMBERID, SecurityContextHelper.getCurrentUserId()).end());
	}

	@Override
	public ResearchExperienceVO getByMember(Long currentUserId, Long id) {
		return this.list(QueryBuilder.create(ResearchExperienceMapping.class)
				.and(ResearchExperienceMapping.MEMBERID, SecurityContextHelper.getCurrentUserId()).and(ResearchExperienceMapping.ID, id).end()).get(0);
	}
}
