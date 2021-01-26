package com.cloudok.base.message.configure;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ConfigurationProperties(prefix = "com.cloudok")
public class MessageProperties {

	private Map<String, MessageBusiness> message;
	
	@Getter @Setter
	public static class MessageBusiness{
		
		private String name;
		
		private Map<String, MessageChannel> channel;
		
		@Getter @Setter
		public static class MessageChannel{
			private MessageTemplate template;
			
			private MessageRetry retry;
			
			private Map<String, String> parameters;
			
			@Getter @Setter
			public static class MessageRetry{
				private boolean enable=false; 
				private int interval[];
			}
			
			@Getter @Setter
			public static class MessageTemplate{
				
				private String param;
				
				private String text;
				
				private String title;
			}
		}
	}
}
