package com.cloudok.base.attach;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.cloudok.base.attach.AttachProperties.AttachBusiness;
import com.cloudok.security.SecurityContextHelper;

/**
 * 附件权限 校验 登录即可上传下载
 * @author xiazhijian
 * @date Jun 28, 2019 4:11:18 PM
 * 
 */
@Component("attachAuthorityDefault")
public class AttachAuthorityDefault implements AttachAuthorityHandle{

	@Override
	public boolean uploader(AttachBusiness config, Map<String, String> params) {
		return SecurityContextHelper.isLogin();
	}

	@Override
	public boolean download(AttachBusiness config, Map<String, String> params) {
		return SecurityContextHelper.isLogin();
	}

}
