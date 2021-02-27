package com.cloudok.base.channel.aliyun.oss;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ConfigurationProperties(prefix = "com.cloudok.channel.aliyun.oss")
public class OSSProperties {

    private String endpoint; 
    private String bucket;
    private String baseDir;
    private Long signTimeout;
	private String accessKey;
	
	private String accessSecret;
	
	private String cdnDomain;
	private String cdnKey;
	
}
