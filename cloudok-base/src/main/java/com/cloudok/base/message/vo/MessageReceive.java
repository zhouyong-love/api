package com.cloudok.base.message.vo;

import com.cloudok.security.User;

import lombok.Getter;
import lombok.Setter;

/**
 * 消息接收者
 * @author xiazhijian
 *
 */
@Getter @Setter
public class MessageReceive {

	
	private Object receiver;
	
	private ReceiveType receiveType;
	
	public MessageReceive() {}
	
	public MessageReceive(Object receiver, ReceiveType receiveType) {
		this.receiver = receiver;
		this.receiveType = receiveType;
	}


	public static enum ReceiveType{
		USER,DIRECT;
	}
	
	@Getter @Setter
	public static class UserMessageReceive extends MessageReceive{
		private User receiver;
		
		public UserMessageReceive() {
			
		}
		public UserMessageReceive(User receiver) {
			this.receiver=receiver;
			setReceiveType(ReceiveType.USER);
		}
	}
	
	@Getter @Setter
	public static class DirectMessageReceive extends MessageReceive{
		
		private String receiver;
		public DirectMessageReceive() {
			
		}
		public DirectMessageReceive(String receiver) {
			this.receiver=receiver;
			setReceiveType(ReceiveType.DIRECT);
		}
	}
}
