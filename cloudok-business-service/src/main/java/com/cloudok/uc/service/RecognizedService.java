package com.cloudok.uc.service;

import java.util.List;

import com.cloudok.core.service.IService;
import com.cloudok.core.vo.Page;
import com.cloudok.uc.po.RecognizedPO;
import com.cloudok.uc.vo.RecognizedVO;

public interface RecognizedService extends IService<RecognizedVO,RecognizedPO>{

	@Deprecated
	int getFriendCount();
	
	int getNewApplyCount();
	
	void read(List<Long> memberIds);
	
	void unRecognized(Long memberId);

	Page<RecognizedVO> getSecondDegreeRecognized(Long currentUserId, Long memberId, Integer pageNo, Integer pageSize);
}
