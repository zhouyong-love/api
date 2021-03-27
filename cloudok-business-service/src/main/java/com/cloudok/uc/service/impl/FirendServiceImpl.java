package com.cloudok.uc.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cloudok.core.event.BusinessEvent;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.service.AbstractService;
import com.cloudok.uc.event.RecognizedCreateEvent;
import com.cloudok.uc.event.RecognizedDeleteEvent;
import com.cloudok.uc.mapper.FirendMapper;
import com.cloudok.uc.mapping.FirendMapping;
import com.cloudok.uc.mapping.RecognizedMapping;
import com.cloudok.uc.po.FirendPO;
import com.cloudok.uc.service.FirendService;
import com.cloudok.uc.service.RecognizedService;
import com.cloudok.uc.vo.FirendVO;
import com.cloudok.uc.vo.RecognizedVO;

@Service
public class FirendServiceImpl extends AbstractService<FirendVO, FirendPO> implements FirendService ,ApplicationListener<BusinessEvent<?>>{
	
	@Autowired
	private RecognizedService recognizedService;
	
	@Autowired
	public FirendServiceImpl(FirendMapper repository) {
		super(repository);
	}
	
	private void recognized(RecognizedVO recognized) {
		//认可某人，如果相互认可了，则相互加入到好友表
		RecognizedVO sourceRecognized = this.recognizedService.get(QueryBuilder.create(RecognizedMapping.class)
				.and(RecognizedMapping.SOURCEID, recognized.getTargetId())
				.and(RecognizedMapping.TARGETID, recognized.getSourceId()).end());
		if(sourceRecognized != null) { //表示相互认可了 source 认可了 target 且 target 认可了 source
			FirendVO f1 = new FirendVO();
			f1.setSourceId(recognized.getSourceId());
			f1.setTargetId(recognized.getTargetId());
			
			FirendVO f2 = new FirendVO();
			f2.setSourceId(recognized.getTargetId());
			f2.setTargetId(recognized.getSourceId());
			//相互创建好友关系
			this.create(f1);
			this.create(f2);
			
		}
		
	}
	
	private void unRecognized(RecognizedVO recognized) {
		//取消认可
		List<FirendVO>	list = this.list(QueryBuilder.create(FirendMapping.class)
				.and(FirendMapping.SOURCEID, recognized.getSourceId()).and(FirendMapping.TARGETID, recognized.getTargetId())
				.end()
				.or(FirendMapping.SOURCEID, recognized.getTargetId()).and(FirendMapping.TARGETID,recognized.getSourceId())
				.end()
				);
		if(!CollectionUtils.isEmpty(list)) {
			list.stream().forEach(item ->{
				this.remove(item.getId()); //删除好友关系
			});
		}
	}

	@Override
	public void onApplicationEvent(BusinessEvent<?> event) {
		if(event.isProcessed(getClass())) {
			return;
		}
		if(event instanceof RecognizedCreateEvent) {
			event.logDetails();
			this.recognized(RecognizedCreateEvent.class.cast(event).getEventData());
		}else if(event instanceof RecognizedDeleteEvent) {
			event.logDetails();
			this.unRecognized(RecognizedDeleteEvent.class.cast(event).getEventData());
		}
		
	}

	@Override
	public boolean isFirends(Long currentUserId, Long memberId) {
		List<FirendVO>	list = this.list(QueryBuilder.create(FirendMapping.class)
				.and(FirendMapping.SOURCEID, currentUserId).and(FirendMapping.TARGETID, memberId)
				.end()
				.or(FirendMapping.SOURCEID, memberId).and(FirendMapping.TARGETID,currentUserId)
				.end()
				);
		return !CollectionUtils.isEmpty(list);
	}
}
