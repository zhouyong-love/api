package com.cloudok.uc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudok.core.service.AbstractService;
import com.cloudok.uc.mapper.RecognizedMapper;
import com.cloudok.uc.po.RecognizedPO;
import com.cloudok.uc.service.RecognizedService;
import com.cloudok.uc.vo.RecognizedVO;

@Service
public class RecognizedServiceImpl extends AbstractService<RecognizedVO, RecognizedPO> implements RecognizedService{

	@Autowired
	public RecognizedServiceImpl(RecognizedMapper repository) {
		super(repository);
	}

	@Override
	public int getFriendCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNewApplyCount() {
		// TODO Auto-generated method stub
		return 0;
	}
}
