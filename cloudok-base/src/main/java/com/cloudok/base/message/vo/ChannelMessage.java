package com.cloudok.base.message.vo;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ChannelMessage {

	private String channel;
	
	private String parameters;
	
	private String receive;
	
	private Map<String, String> channelParameters;
	
	private Long id;
}
