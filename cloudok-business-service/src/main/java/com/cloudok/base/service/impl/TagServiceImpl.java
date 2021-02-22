package com.cloudok.base.service.impl;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudok.base.mapper.TagMapper;
import com.cloudok.base.mapping.TagMapping;
import com.cloudok.base.po.TagPO;
import com.cloudok.base.service.TagService;
import com.cloudok.base.vo.TagVO;
import com.cloudok.core.exception.CoreExceptionMessage;
import com.cloudok.core.exception.SystemException;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.service.AbstractService;
import com.cloudok.enums.TagType;
import com.cloudok.security.SecurityContextHelper;

@Service
public class TagServiceImpl extends AbstractService<TagVO, TagPO> implements TagService {

	@Autowired
	public TagServiceImpl(TagMapper repository) {
		super(repository);
	}

	@Override
	public TagVO createByMember(@Valid TagVO vo) {
		vo.setParentId(0L);
		vo.setType(TagType.CUSTOM.getValue());
		TagVO v = this.get(QueryBuilder.create(TagMapping.class).sort(TagMapping.SN).desc());
		if(v != null) {
			vo.setSn(v.getSn()+1);
		}else {
			vo.setSn(99999);
		}
		return this.create(vo);
	}

	@Override
	public TagVO updateByMember(@Valid TagVO vo) {
		TagVO dbTag = this.get(vo.getId());

		if (dbTag == null || !dbTag.getCreateBy().equals(SecurityContextHelper.getCurrentUserId())
				|| !TagType.CUSTOM.getValue().equals(dbTag.getType())) {
			throw new SystemException(CoreExceptionMessage.NO_PERMISSION);
		}
		vo.setSn(dbTag.getSn());
		return this.update(vo);
	}

	@Override
	public int removeByMember(Long id) {
		TagVO dbTag = this.get(id);
		if (dbTag == null || !dbTag.getCreateBy().equals(SecurityContextHelper.getCurrentUserId())
				|| !TagType.CUSTOM.getValue().equals(dbTag.getType())) {
			throw new SystemException(CoreExceptionMessage.NO_PERMISSION);
		}
		return this.remove(id);
	}
}
