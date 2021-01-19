package com.cloudok.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cloudok.security.token.JWTTokenInfo;
import com.cloudok.security.token.JWTUtil;

/**
 * 
 * @author zhijian.xia@foxmail.com
 * @date 2020年6月17日 下午11:25:30
 */
@Component
public class AuthenticationTokenFilter extends OncePerRequestFilter {

	@Autowired
	private UserInfoHandler userInfoHandler;
	
	@Override
	protected void doFilterInternal(HttpServletRequest arg0, HttpServletResponse arg1, FilterChain arg2)
			throws ServletException, IOException {
		try {
			if (!"OPTIONS".equalsIgnoreCase(arg0.getMethod())) {
   				String token = arg0.getHeader("Authorization");
				if (!StringUtils.isEmpty(token)) {
					if (!StringUtils.isEmpty(token)) {
						JWTTokenInfo t = JWTUtil.decodeToken(token);
						User user = loadUserInfo(arg0, t);
						UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user,
								null, user.getAuthorities());
						authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(arg0));
						SecurityContextHolder.getContext().setAuthentication(authentication);
					}else {
						String _sign=arg0.getParameter("sign");
						if (!StringUtils.isEmpty(_sign)) {
							JWTTokenInfo t =JWTUtil.decodeToken(JWTUtil.getJWTTokenFromProvisionalPass(_sign));
							User user = loadUserInfo(arg0,t);
							user.setProvisionalAuthority(true); //url签名访问
							UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user,
									null, user.getAuthorities());
							authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(arg0));
							SecurityContextHolder.getContext().setAuthentication(authentication);
						}
					}}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		arg2.doFilter(arg0, arg1);
	}
		
	private User loadUserInfo(HttpServletRequest arg0,JWTTokenInfo t) {
		return userInfoHandler.loadUserInfoByToken(t);
	}
}
