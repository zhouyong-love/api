package com.cloudok.base.message.po;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class ReadMessage implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long userId;
	
	private List<Long> messages;
}
