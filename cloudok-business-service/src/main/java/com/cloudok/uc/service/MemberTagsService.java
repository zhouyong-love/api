package com.cloudok.uc.service;

import java.util.List;

import javax.validation.Valid;

import com.cloudok.core.service.IService;
import com.cloudok.uc.po.MemberTagsPO;
import com.cloudok.uc.vo.MemberTagsVO;
import com.cloudok.uc.vo.SwitchSNRequest;

public interface MemberTagsService extends IService<MemberTagsVO,MemberTagsPO>{

	List<MemberTagsVO> getByMember(Long currentUserId);
	
	MemberTagsVO getByMember(Long currentUserId,Long id);

	Object switchSN(@Valid SwitchSNRequest switchSNRequest);

}
