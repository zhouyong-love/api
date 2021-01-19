package com.cloudok.security;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.cloudok.core.exception.CoreExceptionMessage;
import com.cloudok.core.exception.SystemException;
import com.cloudok.core.vo.Response;
import com.cloudok.security.exception.SecurityExceptionMessage;

/**
 * 
 * @author zhijian.xia@foxmail.com
 * @date 2020年6月17日 下午11:28:54
 */
@Configuration
@AutoConfigureBefore(SecurityConfig.class)
public class GlobalExceptionConfigur implements WebMvcConfigurer {

	@Override
	public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {

		resolvers.add(new HandlerExceptionResolver() {

			@Override
			public ModelAndView resolveException(HttpServletRequest arg0, HttpServletResponse arg1,
					Object arg2, Exception arg3) {
				arg3.printStackTrace();
				Response _response;
				if (arg3 instanceof NoHandlerFoundException) {
					_response = Response.buildFail(CoreExceptionMessage.NOTFOUND_ERR);
					arg1.setStatus(404);
				} else if (arg3 instanceof SystemException) {
					SystemException e=(SystemException) arg3;
					_response = Response.buildFail(e);
					arg1.setStatus(500);
				} else if(arg3 instanceof AccessDeniedException){
					if(SecurityContextHelper.isLogin()) {
						_response = Response.buildFail(SecurityExceptionMessage.PERMISSION_DENIED);
						arg1.setStatus(403);
					}else {
						_response = Response.buildFail(SecurityExceptionMessage.NO_AUTHENTICATION);
						arg1.setStatus(401);
					}
				}else {
					_response = Response.buildFail(arg3);
					arg1.setStatus(500);
				}
				
				arg1.setHeader("Content-Type", "application/json;charset=UTF-8");
				try {
					arg1.getWriter().write(_response.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
				return new ModelAndView();
			}
		});
	}
}
