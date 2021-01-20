package com.cloudok.base.service;

import javax.validation.Valid;

import com.cloudok.base.po.TagPO;
import com.cloudok.base.vo.TagVO;
import com.cloudok.core.service.IService;

public interface TagService extends IService<TagVO,TagPO>{

	TagVO createByMember(@Valid TagVO vo);

	TagVO updateByMember(@Valid TagVO vo);

	int removeByMember(Long id);

}
