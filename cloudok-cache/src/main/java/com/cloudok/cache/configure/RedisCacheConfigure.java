package com.cloudok.cache.configure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import lombok.extern.java.Log;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author zhijian.xia@foxmail.com
 * @date 2020年6月24日 上午8:31:00
 */
@Log
@Component
@Configuration
@ConditionalOnProperty(name = "spring.redis.host")
@ConditionalOnMissingBean(JedisCluster.class)
public class RedisCacheConfigure {

	@Autowired
	private CacheRedisProperties redisProperties;
	
	@Bean
    public JedisPoolConfig initJedisPoolConfig()  throws Exception{
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(redisProperties.getLettuce().getPool().getMaxIdle());
        jedisPoolConfig.setMinIdle(redisProperties.getLettuce().getPool().getMinIdle());
        jedisPoolConfig.setMaxWaitMillis(redisProperties.getLettuce().getPool().getMaxWait().toMillis());
        return jedisPoolConfig;
    }
    
    @Bean
    public JedisPool redisPoolFactory(JedisPoolConfig jedisPoolConfig)  throws Exception{
    	log.info("JedisPool注入成功！！");
    	log.info("redis地址：" + redisProperties.getHost() + ":" + redisProperties.getPort());
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, redisProperties.getHost(), redisProperties.getPort(), (int)redisProperties.getTimeout().toMillis(), StringUtils.isEmpty(redisProperties.getPassword())?null:redisProperties.getPassword());
        return jedisPool;
    }
}
