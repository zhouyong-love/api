package com.cloudok.uc.service;

import java.util.List;

import com.cloudok.base.vo.TopicVO;
import com.cloudok.core.service.IService;
import com.cloudok.core.vo.Page;
import com.cloudok.uc.po.MemberTopicPO;
import com.cloudok.uc.vo.MemberTopicVO;
import com.cloudok.uc.vo.TopicDiscoverVO;

public interface MemberTopicService extends IService<MemberTopicVO,MemberTopicPO>{

	Page<TopicDiscoverVO> discover(Integer pageNo, Integer pageSize);

	TopicVO getDetails(Integer topicType, Long topicId);

	List<TopicVO> getSuggestTopics();

}
