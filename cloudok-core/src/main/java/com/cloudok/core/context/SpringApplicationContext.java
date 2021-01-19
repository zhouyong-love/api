package com.cloudok.core.context;

import java.util.Collection;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author xiazhijian
 * @date Apr 15, 2019 4:21:52 PM
 * 
 */
@Component
public class SpringApplicationContext implements ApplicationContextAware{
	
	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringApplicationContext.context=applicationContext;
	}
	
	/**
	 * 获取ApplicationContext
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		return context;
	}
	
	/**
	 * 获取Bean
	 * @param name
	 * @return
	 */
	public static Object getBean(String name) {
		return context.getBean(name);
	}
	
	/**
	 * 获取Bean
	 * @param classz
	 * @return
	 */
	public static <T> T getBean(Class<T> classz) {
		return context.getBean(classz);
	}
	
	/**
	 * 获取Bean
	 * @param name
	 * @param classz
	 * @return
	 */
	public static <T> T getBean(String name,Class<T> classz) {
		return context.getBean(name,classz);
	}
	
	/**
	 * 返回bean的多个实现
	 * @param requiredType
	 * @return
	 * @throws BeansException
	 */
	public static <T>  Collection<T> getBeanOfType(Class<T> requiredType) throws BeansException {
		return context.getBeansOfType(requiredType).values();
	}
	
	/**
	 * 发布事件
	 * @param event
	 * @throws BeansException
	 */
	public static void publishEvent(Object event) throws BeansException {
		context.publishEvent(event);
	}
}
