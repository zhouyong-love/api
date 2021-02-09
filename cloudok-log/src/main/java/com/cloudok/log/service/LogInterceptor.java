package com.cloudok.log.service;

import com.cloudok.log.vo.InterceptorResult;
import com.cloudok.log.vo.LogContext;

public interface LogInterceptor {
	public InterceptorResult process(LogContext context);
}
