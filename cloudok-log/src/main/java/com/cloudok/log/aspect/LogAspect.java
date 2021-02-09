package com.cloudok.log.aspect;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 
 * @author xiazhijian
 *
 */

@Aspect
@Component
public class LogAspect {
	
	@Autowired
	private LogAspectAdapter logAspectAdapter;

	@Pointcut("@annotation(com.cloudok.log.annotation.Loggable)")
	public void logRecord() {
	}

	@AfterThrowing(pointcut = "logRecord()", throwing = "e")
	public void handleThrowing(JoinPoint joinPoint,Exception e) {
		logAspectAdapter.exception(e, ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest(), joinPoint);
	}

	@AfterReturning(returning = "ret", pointcut = "logRecord()")
	public void doAfterReturning(JoinPoint joinPoint,Object ret) throws Throwable {
		logAspectAdapter.response(ret, ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest(),joinPoint);
	}
}
