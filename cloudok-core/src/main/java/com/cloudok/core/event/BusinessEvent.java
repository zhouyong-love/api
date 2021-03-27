package com.cloudok.core.event;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;

public class BusinessEvent<T> extends ApplicationEvent {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	private T data;
	private List<String> processedBeanList = new ArrayList<String>();
	private String eventId;
	public BusinessEvent(T source) {
		super(source);
		this.data = source;
		this.eventId = UUID.randomUUID().toString();
	}
	public boolean isProcessed(Class<?> clazz) {
		boolean processed = processedBeanList.stream().filter(item -> item.equals(clazz.getSimpleName())).findAny().isPresent();
		if(!processed) {
			processedBeanList.add(clazz.getSimpleName());
		}
		return processed;
	}
	public T getEventData() {
		return data;
	} 

	public String getEventId() {
		return eventId;
	}
	
	public void logDetails() {
		logger.info("[{}]事件即将被处理,事件id={},事件参数为={},时间戳为={}",this.getClass().getSimpleName(),
				this.getEventId(),com.cloudok.core.json.JSON.toJSONString(this.getEventData()),this.getTimestamp());
	}
	 
}