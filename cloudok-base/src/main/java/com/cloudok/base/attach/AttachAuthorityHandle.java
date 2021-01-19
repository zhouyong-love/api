package com.cloudok.base.attach;

import java.util.Map;

import com.cloudok.base.attach.AttachProperties.AttachBusiness;

/**
 * 附件读写权限接口
 * @author xiazhijian
 *
 */
public interface AttachAuthorityHandle {
	
	/**
	 * 附件上传权限校验
	 * @param config
	 * @param params
	 * @return
	 */
	boolean uploader(AttachBusiness config,Map<String, String> params);
	
	/**
	 * 附件下载权限校验
	 * @param config
	 * @param params
	 * @return
	 */
	boolean download(AttachBusiness config,Map<String, String> params);
}
