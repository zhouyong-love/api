package com.cloudok.uc.service;

import java.util.List;

import com.cloudok.core.service.IService;
import com.cloudok.uc.po.MemberTagsPO;
import com.cloudok.uc.vo.MemberTagsVO;

public interface MemberTagsService extends IService<MemberTagsVO,MemberTagsPO>{

	List<MemberTagsVO> getByMember(Long currentUserId);

}
