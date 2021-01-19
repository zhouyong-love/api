package com.cloudok.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import com.cloudok.security.token.TokenProperties;

/**
 * 
 * @author zhijian.xia@foxmail.com
 * @date 2020年6月17日 下午11:17:18
 */
@Configuration
@EnableWebSecurity
@Order(Integer.MAX_VALUE)
@EnableGlobalMethodSecurity(prePostEnabled=true)
@EnableAutoConfiguration(exclude = UserDetailsServiceAutoConfiguration.class)
@EnableConfigurationProperties(TokenProperties.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AccessDeniedHandler accessDeniedHandler;

	@Autowired
	private AuthenticationEntryPoint authenticationEntryPoint;

	@Autowired
	private AuthenticationTokenFilter authenticationTokenFilter;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and().exceptionHandling().accessDeniedHandler(accessDeniedHandler)
		.authenticationEntryPoint(authenticationEntryPoint).and()
		.addFilterBefore(authenticationTokenFilter, FilterSecurityInterceptor.class).authorizeRequests()
		.anyRequest().permitAll();
	}

	
	@Bean
	public org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder bcryptPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}
}
