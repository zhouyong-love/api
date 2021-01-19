package com.cloudok.cache;

import java.util.concurrent.TimeUnit;
/**
 * 缓存
 * @author xiazhijian
 *
 */
public interface Cache {

	/**
	 * 设置缓存
	 * @param <T>
	 * @param cacheType 缓存类型
	 * @param key	缓存键
	 * @param value	缓存值
	 */
	<T> void put(CacheNameSpace cacheType,String key,T value);
	
	/**
	 * 设置缓存
	 * @param <T>
	 * @param cacheType
	 * @param key
	 * @param value
	 * @param expire
	 */
	<T> void put(CacheNameSpace cacheType,String key,T value,int expire);
	
	/**
	 * 设置缓存
	 * @param <T>
	 * @param cacheType
	 * @param key
	 * @param value
	 * @param expire
	 * @param unit
	 */
	<T> void put(CacheNameSpace cacheType,String key,T value,int expire,TimeUnit unit);

	/**
	 * 获取缓存
	 * @param <T>
	 * @param cacheType
	 * @param key
	 * @param type
	 * @return
	 */
	<T> T get(CacheNameSpace cacheType,String key,Class<T> type);
	
	/**
	 * 废弃缓存
	 * @param cacheType
	 * @param key
	 */
	void evict(CacheNameSpace cacheType, String key);
	
	/**
	 * 设置缓存有效期
	 * @param cacheType
	 * @param key
	 * @param expire
	 * @param unit
	 */
	void expire(CacheNameSpace cacheType, String key, int expire, TimeUnit unit);
	
	/**
	 * 设置缓存有效期
	 * @param cacheType
	 * @param key
	 * @param expire
	 */
	void expire(CacheNameSpace cacheType, String key, int expire);

	/**
	 * 判断缓存是否存在
	 * @param type
	 * @param key
	 * @return
	 */
	boolean exist(CacheNameSpace type, String key);
	
	default String buildCacheKey (CacheNameSpace type,String key) {
		return type.getNameSpace()+"."+key;
	}
}
