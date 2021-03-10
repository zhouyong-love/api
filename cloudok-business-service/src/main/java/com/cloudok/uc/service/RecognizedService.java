package com.cloudok.uc.service;

import java.util.List;

import com.cloudok.core.service.IService;
import com.cloudok.core.vo.Page;
import com.cloudok.uc.po.RecognizedPO;
import com.cloudok.uc.vo.RecognizedVO;

import lombok.Builder;
import lombok.Data;

public interface RecognizedService extends IService<RecognizedVO,RecognizedPO>{

	@Deprecated
	int getFriendCount();
	
	int getNewApplyCount();
	
	void read(List<Long> memberIds);
	
	RecognizedTotalDTO unRecognized(Long memberId);

	Page<RecognizedVO> getSecondDegreeRecognized(Long currentUserId, Long memberId, Integer pageNo, Integer pageSize);
	
	RecognizedTotalDTO recognized(RecognizedVO vo);
	
	@Builder
	@Data
	public static class RecognizedTotalDTO{
		
		private long friendCount;
		
		private long fromCount;
		
		private long toCount;
		
	}
}
