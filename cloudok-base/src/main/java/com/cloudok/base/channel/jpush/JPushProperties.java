package com.cloudok.base.channel.jpush;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ConfigurationProperties(prefix = "com.cloudok.channel.jpush")
public class JPushProperties {

    private String appKey;
    
    private String masterSecret;
}
