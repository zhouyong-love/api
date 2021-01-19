package com.cloudok.authority.service.grant;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注载体VO中用于显示的name
 * @author xiazhijian
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GrantObjectField {
	
	/**
	 * 字段显示标题
	 * @return
	 */
	String fieldLabel();
	
	/**
	 * 是否为检索字段
	 * @return
	 */
	boolean searchField() default false;
	
	/**
	 * 字段显示顺序
	 * @return
	 */
	int order() default 0;
}
