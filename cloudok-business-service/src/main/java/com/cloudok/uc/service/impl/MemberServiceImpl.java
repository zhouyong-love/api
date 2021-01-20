package com.cloudok.uc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudok.core.service.AbstractService;
import com.cloudok.uc.mapper.MemberMapper;
import com.cloudok.uc.po.MemberPO;
import com.cloudok.uc.service.MemberService;
import com.cloudok.uc.vo.MemberVO;

@Service
public class MemberServiceImpl extends AbstractService<MemberVO, MemberPO> implements MemberService{

	@Autowired
	public MemberServiceImpl(MemberMapper repository) {
		super(repository);
	}
}
