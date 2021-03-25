package com.cloudok.uc.task;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.cloudok.base.task.TopicSyncTask;
import com.cloudok.bbs.mapper.PostMapper;
import com.cloudok.bbs.mapping.PostMapping;
import com.cloudok.bbs.po.PostPO;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.enums.BBSTopicType;
import com.cloudok.uc.service.MemberTagsService;

@Component
public class MemberTagsSysncTask implements InitializingBean{

	@Autowired
	private PostMapper postMapper;
	
	@Autowired
	private MemberTagsService memberTagsService;
	
	@Autowired
	private TopicSyncTask topicSyncTask;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		new Thread(()->{
			try {
				Thread.sleep(TimeUnit.MINUTES.toMillis(1));
			} catch (InterruptedException e) {
			}
			 this.calcAll();
		}) .start();
		
	}

	private void calcAll() {
		int pageIndex = 1;
		List<PostPO> postList = postMapper.select(QueryBuilder.create(PostMapping.class).sort(PostMapping.ID).desc().enablePaging().page(pageIndex, 50).end());
		while(!CollectionUtils.isEmpty(postList)) {
			postList.stream().forEach(post ->{
				if(BBSTopicType.systemSuggestTag.getValue().equals(post.getTopicType().toString())) { //是系统推荐标签
					//新增新标签关联
					memberTagsService.sysnc(post.getCreateBy(), post.getTopicId());
				}
				 
			});
			pageIndex = pageIndex+1;
			postList = postMapper.select(QueryBuilder.create(PostMapping.class).sort(PostMapping.ID).desc().enablePaging().page(pageIndex, 50).end());
		}
		//trigger topic sync
		try {
			topicSyncTask.afterPropertiesSet();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
