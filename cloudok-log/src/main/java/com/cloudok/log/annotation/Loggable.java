package com.cloudok.log.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.cloudok.log.enums.LogSwitch;
import com.cloudok.log.enums.SysLogLevel;
import com.cloudok.log.service.LogInterceptor;
import com.cloudok.log.service.impl.DefaultLogInterceptor;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Loggable {

	/**
	 * 接口编号
	 * @return
	 */
	public String code() default "";
	
	/**
	 * 接口名称
	 * @return
	 */
	public String name() default "";
	
	/**
	 * 入参记录开关
	 * @return
	 */
	public LogSwitch input() default LogSwitch.DEFAULT;
	
	/**
	 * 出参记录开关
	 * @return
	 */
	public LogSwitch output() default LogSwitch.DEFAULT;
	
	/**
	 * 日志记录级别
	 * @return
	 */
	public SysLogLevel level() default SysLogLevel.DEFAULT;
	
	public Class<? extends LogInterceptor> interceptor() default DefaultLogInterceptor.class;
	
	public String messageTemplate() default "";
	
}
