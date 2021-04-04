package com.cloudok.common;

import com.cloudok.cache.CacheNameSpace;

public class CacheType {
	public static final CacheNameSpace Member =new CacheNameSpace("uc", "member");
	
	public static final CacheNameSpace VerifyCode = new CacheNameSpace("uc", "VerifyCode");
	
	public static final CacheNameSpace VerifyCodeCount = new CacheNameSpace("uc", "VerifyCodeCount");
	
	public static final CacheNameSpace Action = new CacheNameSpace("uc", "Action");
	
	public static final CacheNameSpace RI = new CacheNameSpace("uc", "RI");
	
	public static final CacheNameSpace SuggestHistory = new CacheNameSpace("uc", "suggest");
	
	public static final CacheNameSpace SuggestStream = new CacheNameSpace("uc", "sgs");
}
