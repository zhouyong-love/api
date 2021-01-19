package com.cloudok.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import com.cloudok.core.vo.Response;
import com.cloudok.security.exception.SecurityExceptionMessage;

/**
 * 
 * @author zhijian.xia@foxmail.com
 * @date 2020年6月17日 下午11:17:43
 */
@Component
public class AccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler{

	@Override
	public void handle(HttpServletRequest arg0, HttpServletResponse arg1, AccessDeniedException arg2)
			throws IOException, ServletException {
		arg1.setStatus(403);
		arg1.setCharacterEncoding("UTF-8");
		arg1.setContentType("application/json; charset=utf-8");
        PrintWriter printWriter = arg1.getWriter();
        printWriter.write(Response.buildFail(SecurityExceptionMessage.PERMISSION_DENIED).toString());
        printWriter.flush();
	}
}
