package com.cloudok.common;

import com.cloudok.cache.CacheNameSpace;

public class CacheType {
	public static final CacheNameSpace Member =new CacheNameSpace("uc", "member");
	
	public static final CacheNameSpace VerifyCode = new CacheNameSpace("uc", "VerifyCode");
	
	public static final CacheNameSpace VerifyCodeCount = new CacheNameSpace("uc", "VerifyCodeCount");
}