package com.cloudok.core.context;

public interface SecurityContextAdapter {

	Long getCurrentUserId();
	
	
	String getCurrentUserName();
	
	
	Long getTenantId();
}
