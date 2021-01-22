package com.cloudok.base.channel.jpush;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.jiguang.common.ClientConfig;
import cn.jpush.api.JPushClient;

@Configuration
@EnableConfigurationProperties(JPushProperties.class)
public class JPushConfigure {

	@Autowired
	private JPushProperties guangProperties;

	@Bean
    public JPushClient jpushClientConfig(){
        JPushClient jPushClient = new JPushClient(guangProperties.getMasterSecret(),guangProperties.getAppKey(), null, ClientConfig.getInstance());
        return jPushClient;
    }
}
