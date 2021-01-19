package com.cloudok.security;

import org.springframework.stereotype.Component;

import com.cloudok.core.context.SecurityContextAdapter;

/**
 * @author zhijian.xia@foxmail.com
 * @date 2020年6月18日 上午11:09:56
 */
@Component
public class SecurityContextHandler implements SecurityContextAdapter{

	@Override
	public Long getCurrentUserId() {
		return SecurityContextHelper.isLogin()?SecurityContextHelper.getCurrentUserId():0L;
	}

	@Override
	public String getCurrentUserName() {
		return SecurityContextHelper.getCurrentUserName();
	}

	@Override
	public Long getTenantId() {
		return 0L;
	}

}
