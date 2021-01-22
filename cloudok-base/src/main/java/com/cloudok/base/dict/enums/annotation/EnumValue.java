package com.cloudok.base.dict.enums.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhijian.xia@foxmail.com
 * @date 2020年6月29日 下午10:58:08
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumValue {

	long sn();
}