package com.cloudok.base.service;

import com.cloudok.core.service.IService;
import com.cloudok.base.po.TopicPO;
import com.cloudok.base.vo.TopicVO;

public interface TopicService extends IService<TopicVO,TopicPO>{

	TopicVO getDetails(Integer topicType, Long topicId);

}
