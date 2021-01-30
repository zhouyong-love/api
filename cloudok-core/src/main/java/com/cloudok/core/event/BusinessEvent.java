package com.cloudok.core.event;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;

public class BusinessEvent<T> extends ApplicationEvent {
	private static final long serialVersionUID = 1L;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private T data;
	private List<String> processedBeanList = new ArrayList<String>();
	public BusinessEvent(T source) {
		super(source);
		this.data = source;
	}
	public boolean isProcessed(Class<?> clazz) {
		boolean processed = processedBeanList.stream().filter(item -> item.equals(clazz.getSimpleName())).findAny().isPresent();
		if(!processed) {
			processedBeanList.add(clazz.getSimpleName());
		}
		return processed;
	}
	public T getEventData() {
		logger.info("[{}]事件即将被处理,事件参数为={},时间戳为={}",this.getClass().getSimpleName(),com.cloudok.core.json.JSON.toJSONString(data),this.getTimestamp());
		return data;
	}
	
	
	
	
}