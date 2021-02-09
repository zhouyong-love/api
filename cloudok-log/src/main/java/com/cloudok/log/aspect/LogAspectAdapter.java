package com.cloudok.log.aspect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.cloudok.core.context.SpringApplicationContext;
import com.cloudok.core.vo.Response;
import com.cloudok.log.annotation.LogModule;
import com.cloudok.log.annotation.Loggable;
import com.cloudok.log.enums.LogSwitch;
import com.cloudok.log.enums.SysLogLevel;
import com.cloudok.log.service.LogInterceptor;
import com.cloudok.log.service.SysLogService;
import com.cloudok.log.service.impl.DefaultLogInterceptor;
import com.cloudok.log.vo.InterceptorResult;
import com.cloudok.log.vo.LogConfig;
import com.cloudok.log.vo.LogContext;
import com.cloudok.log.vo.SysLogVO;
import com.cloudok.security.SecurityContextHelper;
import com.cloudok.security.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Component
public class LogAspectAdapter {

	private ExecutorService executor = Executors.newFixedThreadPool(10); // 最多同时8个线程并行

	@Autowired
	private SysLogService loggerService;

	@Autowired
	private DefaultLogInterceptor defaultLogInterceptor;

	private static final Logger logger = LoggerFactory.getLogger(LogAspectAdapter.class);

	private Map<String, LogConfig> cached = new ConcurrentHashMap<String, LogConfig>();

	@Value("${logrecord.level:DEFAULT}")
	private String level;

	private transient Integer _level = null;

	private void init() {
		if (_level == null) {
			synchronized (this) {
				if (_level == null) {
					this._level = SysLogLevel.valueOf(level).getLevel();
				}
			}
		}
	}

	public void response(Object ret, HttpServletRequest request, JoinPoint joinPoint) {
		recod(request, ret, joinPoint, 200, null);
	}

	public void exception(Exception ex, HttpServletRequest request, JoinPoint joinPoint) {
		Response b = Response.buildFail(ex);
		recod(request, b, joinPoint, b.getResponseCode(), ex);
	}

	private void recod(HttpServletRequest request, Object result, JoinPoint joinPoint, int httpCode, Exception ex) {
		init();
		MethodSignature methodSignature = MethodSignature.class.cast(joinPoint.getSignature());
		Loggable loggable = methodSignature.getMethod().getAnnotation(Loggable.class);
		LogModule logModule = joinPoint.getTarget().getClass().getAnnotation(LogModule.class);
		List<Object> inputParameterList = new ArrayList<>();
		boolean isHttpMethod = isHttpMethod(methodSignature.getMethod());
		Response response = Response.buildSuccess();
		if (logModule == null) {
			if (logger.isErrorEnabled()) {
				logger.error("API '" + joinPoint.getTarget().getClass().getName() + "' 缺少注解 @LogModule 记录失败！！！");
			}
			throw new RuntimeException(
					"API '" + joinPoint.getTarget().getClass().getName() + "' 缺少注解 @LogModule 记录失败！！！");
		}
		Object params[] = joinPoint.getArgs();
		if (params != null && loggable.input() != LogSwitch.OFF) {
			for (Object param : params) {
				if (param instanceof ServletRequest || param instanceof ServletResponse) {
					continue;
				}
				inputParameterList.add(param);
			}
		}
		if (loggable.output() != LogSwitch.OFF) {
			if (response instanceof Response) {
				BeanUtils.copyProperties(result, response);
			} else {
				response.setResult(result);
			}
			/**
			 * 判断是否是http请求，如果是，则默认不记录返回参数的body
			 */
			if (isHttpMethod && loggable.output() == LogSwitch.DEFAULT) {
				response.setResult(null);
			}
		}
		LogConfig moduleConfig = this.getLogConfig(joinPoint.getTarget().getClass(), logModule);
		LogConfig interfaceConfig = this.getLogConfig(methodSignature, loggable);
		/**
		 * 异步执行提交到数据库
		 */
		User user = SecurityContextHelper.isLogin() ? SecurityContextHelper.getCurrentUser() : null;

		LogInterceptor logInterceptor = this.getLogInterceptor(logModule, loggable);

		InterceptorResult interceptorResult = logInterceptor.process(this.buildLogContext(logModule, loggable, moduleConfig, interfaceConfig, joinPoint, ex,
				request, result));

		executor.submit(() -> {
			loggerService
					.create(SysLogVO.build(
							moduleConfig.getCode(),
							moduleConfig.getName(), 
							interfaceConfig.getCode(),
							interfaceConfig.getName(), 
							loggable.input(), 
							loggable.output(), 
							request.getRequestURI(),
							request.getMethod(),
							httpCode, 
							loggable.level(), 
							inputParameterList, 
							response, 
							user,
							interceptorResult.getBusinessId(),
							interceptorResult.getRelationBusinessId(),
							interceptorResult.getFormatedMessage()
							));
		});
	}

	private LogContext buildLogContext(LogModule logModule, Loggable loggable, LogConfig moduleConfig,
			LogConfig interfaceConfig, JoinPoint joinPoint, Exception ex, HttpServletRequest request, Object ret) {
		LogContext context = new LogContext();
		context.setEx(ex);
		context.setInterfaceConfig(interfaceConfig);
		context.setJoinPoint(joinPoint);
		context.setLoggable(loggable);
		context.setLoggable(loggable);
		context.setLogModule(logModule);
		context.setModuleConfig(moduleConfig);
		context.setRequest(request);
		context.setRet(ret);
		return context;
	}

	private LogInterceptor getLogInterceptor(LogModule logModule, Loggable loggable) {
		Class<? extends LogInterceptor> logInterceptor = null;
		if (loggable.interceptor() != null && loggable.interceptor() != DefaultLogInterceptor.class) {
			logInterceptor = loggable.interceptor();
		}
		if (logInterceptor == null) {
			logInterceptor = logModule.interceptor();
		}
		if (logInterceptor != null && logInterceptor != DefaultLogInterceptor.class) {
			return SpringApplicationContext.getBean(logInterceptor);
		}
		return this.defaultLogInterceptor;
	}

	private boolean isHttpMethod(Method method) {
		return method.getAnnotation(GetMapping.class) != null || method.getAnnotation(PutMapping.class) != null
				|| method.getAnnotation(PostMapping.class) != null || method.getAnnotation(DeleteMapping.class) != null
				|| method.getAnnotation(PatchMapping.class) != null;
	}

	private LogConfig getLogConfig(MethodSignature methodSignature, Loggable loggable) {
		LogConfig config = cached.get(methodSignature.toLongString());
		if (config == null) {
			synchronized (methodSignature) {
				config = cached.get(methodSignature.toLongString());
				if (config == null) {
					String code = loggable.code();
					String name = loggable.name();
					if (StringUtils.isEmpty(name)) {
						// 拿api上的主键
						ApiOperation apiOperation = methodSignature.getMethod().getAnnotation(ApiOperation.class);
						if (apiOperation == null) {
							name = StringUtils.isEmpty(name) ? methodSignature.getMethod().getName() : name;
						} else {
							name = StringUtils.isEmpty(name) ? apiOperation.value() : name;
						}
					}
					if (StringUtils.isEmpty(code)) {
						code = methodSignature.getMethod().getName();
					}
					config = new LogConfig(code, name);
					cached.put(methodSignature.toLongString(), config);
				}
			}
		}
		return config;
	}

	private LogConfig getLogConfig(Class<?> clazz, LogModule logModule) {
		LogConfig config = cached.get(clazz.getName());
		if (config == null) {
			synchronized (clazz) {
				config = cached.get(clazz.getName());
				if (config == null) {
					String code = logModule.code();
					String name = logModule.name();
					if (StringUtils.isEmpty(name)) {
						// 拿api上的主键
						Api apiOperation = clazz.getAnnotation(Api.class);
						if (apiOperation == null) {
							name = StringUtils.isEmpty(name) ? clazz.getSimpleName() : name;
						} else {
							name = StringUtils.isEmpty(name) ? (!StringUtils.isEmpty(apiOperation.value()) ? apiOperation.value(): apiOperation.tags()[0]): name;
						}
					}
					if (StringUtils.isEmpty(code)) {
						code = clazz.getSimpleName();
					}
					config = new LogConfig(code, name);
					cached.put(clazz.getName(), config);
				}
			}
		}
		return config;
	}
}
