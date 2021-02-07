package com.cloudok.uc.service;

import com.cloudok.core.service.IService;
import com.cloudok.uc.po.FirendPO;
import com.cloudok.uc.vo.FirendVO;

public interface FirendService extends IService<FirendVO,FirendPO>{

	boolean isFirends(Long currentUserId, Long memberId);

}
