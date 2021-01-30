package com.cloudok.bbs.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cloudok.bbs.mapper.PostTopicMapper;
import com.cloudok.bbs.po.PostTopicPO;
import com.cloudok.bbs.service.PostTopicService;
import com.cloudok.bbs.service.TopicService;
import com.cloudok.bbs.vo.PostTopicVO;
import com.cloudok.bbs.vo.TopicVO;
import com.cloudok.core.service.AbstractService;

@Service
public class PostTopicServiceImpl extends AbstractService<PostTopicVO, PostTopicPO> implements PostTopicService {
	@Autowired
	private TopicService topicService;
	
	@Autowired
	public PostTopicServiceImpl(PostTopicMapper repository) {
		super(repository);
	}

	@Override
	public PostTopicPO convert2PO(PostTopicVO d) {
		PostTopicPO po = super.convert2PO(d);
		if (d.getTopic() != null) {
			po.setTopicId(d.getTopic().getId());
		}
		return po;
	}

	@Override
	public PostTopicVO convert2VO(PostTopicPO e) {
		PostTopicVO vo = super.convert2VO(e);
		if (e.getTopicId() != null) {
			vo.setTopic(new TopicVO(e.getTopicId()));
		}
		return vo;
	}
	@Override
	public List<PostTopicVO> convert2VO(List<PostTopicPO> e) {
		List<PostTopicVO> list = super.convert2VO(e);
		if(!CollectionUtils.isEmpty(list)) {
			List<Long> idList = list.stream().map(item -> item.getTopic().getId()).distinct().collect(Collectors.toList());
			List<TopicVO> topicList  = topicService.get(idList);
			list.stream().forEach(item ->{
				topicList.stream().filter( topic -> topic.getId().equals(item.getTopic().getId())).findAny().ifPresent(topic -> {
					item.setTopic(topic);
				});
			});
		}
		return list;
	}
}
