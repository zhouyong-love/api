package com.cloudok.cache.exception;

import com.cloudok.core.exception.ExceptionMessage;

/**
 * @author zhijian.xia@foxmail.com
 * @date 2020年6月24日 上午10:22:15
 */
public class CacheExceptionMessage extends ExceptionMessage{

	/**
	 * @param code
	 * @param message
	 */
	public CacheExceptionMessage(String code, String message) {
		super(code, message);
	}

	private static final long serialVersionUID = 5481467497499820409L;

	public static final CacheExceptionMessage CACHE_REDIS_ERROR=new CacheExceptionMessage("CACHE.REDIS_ERROR", "Redis failed to read and write.");
}
