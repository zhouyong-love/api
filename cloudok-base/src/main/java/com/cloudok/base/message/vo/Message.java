package com.cloudok.base.message.vo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Message implements Serializable{

	private static final long serialVersionUID = 4672905299239334004L;

	private String business;

	private String params;
	
	private MessageReceive[] receives;
	
}
