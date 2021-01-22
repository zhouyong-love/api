package com.cloudok.message.utils;

import java.util.List;

import com.cloudok.core.context.SpringApplicationContext;
import com.cloudok.core.json.JSON;
import com.cloudok.message.service.MessageService;
import com.cloudok.message.vo.Message;
import com.cloudok.message.vo.MessageReceive;
import com.cloudok.message.vo.MessageVO;

public class MessageUtil {
	/**
	 * 发送消息
	 * @param <T>
	 * @param business
	 * @param object
	 * @param receive  UserMessageReceive 发送给用户 DirectMessageReceive 发送给直接接受对象比如手机号
	 * @return MessageVO
	 */
	@SafeVarargs
	public static <T extends MessageReceive,Param> List<MessageVO> send(String business,Param parameter,T ...receive) {
		return SpringApplicationContext.getBean(MessageService.class).createMessage(new Message(business, JSON.toJSONString(parameter), receive));
	}
}
