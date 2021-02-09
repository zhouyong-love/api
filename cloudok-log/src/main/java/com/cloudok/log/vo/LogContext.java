package com.cloudok.log.vo;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import com.cloudok.log.annotation.LogModule;
import com.cloudok.log.annotation.Loggable;

public class LogContext {
	private LogModule logModule;
	private Loggable loggable;
	private LogConfig moduleConfig;
	private LogConfig interfaceConfig;
	private JoinPoint joinPoint;
	private Exception ex;
	private HttpServletRequest request;
	private Object ret;
	private MethodSignature methodSignature;

	public LogModule getLogModule() {
		return logModule;
	}

	public void setLogModule(LogModule logModule) {
		this.logModule = logModule;
	}

	public Loggable getLoggable() {
		return loggable;
	}

	public void setLoggable(Loggable loggable) {
		this.loggable = loggable;
	}

	public LogConfig getModuleConfig() {
		return moduleConfig;
	}

	public void setModuleConfig(LogConfig moduleConfig) {
		this.moduleConfig = moduleConfig;
	}

	public LogConfig getInterfaceConfig() {
		return interfaceConfig;
	}

	public void setInterfaceConfig(LogConfig interfaceConfig) {
		this.interfaceConfig = interfaceConfig;
	} 

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public Object getRet() {
		return ret;
	}

	public void setRet(Object ret) {
		this.ret = ret;
	}

	public Exception getEx() {
		return ex;
	}

	public void setEx(Exception ex) {
		this.ex = ex;
	}

	public JoinPoint getJoinPoint() {
		return joinPoint;
	}

	public void setJoinPoint(JoinPoint joinPoint) {
		this.joinPoint = joinPoint;
	}

	public MethodSignature getMethodSignature() {
		return methodSignature;
	}

	public void setMethodSignature(MethodSignature methodSignature) {
		this.methodSignature = methodSignature;
	}
	
	

}
