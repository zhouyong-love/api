package com.cloudok.cache.handler;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import com.cloudok.cache.Cache;
import com.cloudok.cache.CacheNameSpace;
import com.cloudok.cache.configure.RedisCacheConfigure;
import com.cloudok.cache.exception.CacheExceptionMessage;
import com.cloudok.core.exception.SystemException;
import com.cloudok.util.ObjectSerializer;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author zhijian.xia@foxmail.com
 * @date 2020年6月24日 上午10:16:45
 */
@ConditionalOnBean(RedisCacheConfigure.class)
@Component
public class RedisCacheHandler implements Cache{

	@Autowired
	private JedisPool jedisPool;

	@Override
	public <T> void put(CacheNameSpace cacheNameSpace, String key, T value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.set(buildCacheKey(cacheNameSpace, key).getBytes(), ObjectSerializer.serializer(value));
		} catch (Exception e) {
			e.printStackTrace();
			throw new SystemException(CacheExceptionMessage.CACHE_REDIS_ERROR);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public <T> void put(CacheNameSpace cacheNameSpace, String key, T value, int expire) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.setex(buildCacheKey(cacheNameSpace, key).getBytes(), expire, ObjectSerializer.serializer(value));
		} catch (Exception e) {
			throw new SystemException(CacheExceptionMessage.CACHE_REDIS_ERROR);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public <T> void put(CacheNameSpace cacheNameSpace, String key, T value, int expire, TimeUnit unit) {
		put(cacheNameSpace, key, value, (int) unit.toSeconds(expire));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(CacheNameSpace cacheNameSpace, String key, Class<T> type) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return (T) ObjectSerializer.deserializer(jedis.get(buildCacheKey(cacheNameSpace, key).getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
			throw new SystemException(CacheExceptionMessage.CACHE_REDIS_ERROR);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public void evict(CacheNameSpace cacheNameSpace, String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.del(buildCacheKey(cacheNameSpace, key).getBytes());
		} catch (Exception e) {
			e.printStackTrace();
			throw new SystemException(CacheExceptionMessage.CACHE_REDIS_ERROR);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public void expire(CacheNameSpace cacheNameSpace, String key, int expire, TimeUnit unit) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.expire(buildCacheKey(cacheNameSpace, key).getBytes(), (int) unit.toSeconds(expire));
		} catch (Exception e) {
			e.printStackTrace();
			throw new SystemException(CacheExceptionMessage.CACHE_REDIS_ERROR);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public boolean exist(CacheNameSpace cacheNameSpace, String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.exists(buildCacheKey(cacheNameSpace, key).getBytes());
		} catch (Exception e) {
			e.printStackTrace();
			throw new SystemException(CacheExceptionMessage.CACHE_REDIS_ERROR);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public void expire(CacheNameSpace cacheNameSpace, String key, int expire) {
		expire(cacheNameSpace, key, expire, TimeUnit.SECONDS);
	}

}
