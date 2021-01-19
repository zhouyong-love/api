package com.cloudok.cache.configure;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zhijian.xia@foxmail.com
 * @date 2020年6月24日 上午10:31:13
 */
@Component
@ConfigurationProperties(prefix = "spring.redis")
public class CacheRedisProperties extends RedisProperties{

}
