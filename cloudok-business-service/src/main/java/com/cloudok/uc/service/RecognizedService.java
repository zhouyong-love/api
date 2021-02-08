package com.cloudok.uc.service;

import java.util.List;

import com.cloudok.core.service.IService;
import com.cloudok.uc.po.RecognizedPO;
import com.cloudok.uc.vo.RecognizedVO;

public interface RecognizedService extends IService<RecognizedVO,RecognizedPO>{

	int getFriendCount();
	
	int getNewApplyCount();
	
	void read(List<Long> memberIds);
	
	void unRecognized(Long memberId);
}
