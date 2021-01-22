package com.cloudok.base.channel.aliyun.oss;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;


@Configuration
@EnableConfigurationProperties(OSSProperties.class)
public class OSSConfigure {

	@Autowired
	private OSSProperties ossProperties;

	@Bean
	public OSS ossClient() {
		return new OSSClientBuilder().build(ossProperties.getEndpoint(), ossProperties.getAccessKey(),ossProperties.getAccessSecret());
	}
}
