package com.cloudok.base.attach;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.cloudok.base.attach.AttachProperties.AttachBusiness;
import com.cloudok.core.context.SpringApplicationContext;

/**
 * 附件配置解析
 * @author xiazhijian
 *
 */
@Configuration
@EnableConfigurationProperties(AttachProperties.class)
public class AttachConfigure {
	
	public static AttachBusiness getConfig(String business) {
		return SpringApplicationContext.getBean(AttachProperties.class).getBusiness().get(business);
	}
}
