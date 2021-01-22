package com.cloudok.base.message.service.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.cloudok.base.message.channel.ChannelHandler;
import com.cloudok.base.message.configure.MessageProperties;
import com.cloudok.base.message.configure.MessageProperties.MessageBusiness;
import com.cloudok.base.message.enums.MessageStatus;
import com.cloudok.base.message.exception.MessageExceptionMessges;
import com.cloudok.base.message.mapper.MessageMapper;
import com.cloudok.base.message.mapping.MessageMapping;
import com.cloudok.base.message.po.MessagePO;
import com.cloudok.base.message.po.ReadMessage;
import com.cloudok.base.message.service.MessageDetailsService;
import com.cloudok.base.message.service.MessageService;
import com.cloudok.base.message.vo.ChannelMessage;
import com.cloudok.base.message.vo.Message;
import com.cloudok.base.message.vo.MessageDetailsVO;
import com.cloudok.base.message.vo.MessageReceive;
import com.cloudok.base.message.vo.MessageVO;
import com.cloudok.core.exception.SystemException;
import com.cloudok.core.json.JSON;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.query.QueryOperator;
import com.cloudok.core.service.AbstractService;
import com.cloudok.core.vo.Page;
import com.cloudok.security.SecurityContextHelper;
import com.cloudok.security.User;
import com.fasterxml.jackson.databind.JsonNode;

import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MessageServiceImpl extends AbstractService<MessageVO, MessagePO> implements MessageService{

	
	@Getter @Setter @NoArgsConstructor @AllArgsConstructor
	public static class MessageType{
		
		private String messageTypeCode;
		
		private String messageTypeName;
	}
	
	@Autowired
	@Qualifier("messageFreemarkTemplate")
	private freemarker.template.Configuration freemarkerTemplateConfiguration ;
	
	@Autowired
	private MessageDetailsService messageDetailService;
	
	@Autowired
	private MessageMapper messageDao;

	@Autowired
	private MessageProperties messageProperties;
	
	@Autowired
	private ChannelHandler channelHandler;
	

	@Autowired
	public MessageServiceImpl(MessageMapper repository) {
		super(repository);
	}

	
	@Override
	public List<MessageType> messageTypes() {
		List<MessageType> list=new ArrayList<MessageServiceImpl.MessageType>();
		if(!CollectionUtils.isEmpty(messageProperties.getMessage())) {
			messageProperties.getMessage().forEach((k,v)->{
				list.add(new MessageType(k,v.getName()));
			});
			return list;
		}
		return Collections.emptyList();
	}
	 
	
	@Transactional
	@Override
	public List<MessageVO> createMessage(Message message) {
		if(message.getReceives()==null||message.getReceives().length==0) {
			throw new SystemException("没有接收人!",MessageExceptionMessges.RECEIVER_NOT_EXISTS);
		}
		List<MessageVO> messages=new ArrayList<MessageVO>();
		if(!messageProperties.getMessage().containsKey(message.getBusiness())) {
			throw new SystemException(MessageExceptionMessges.build("消息 "+message.getBusiness()+" 配置不存在"));
		}
		MessageBusiness messageBusiness=messageProperties.getMessage().get(message.getBusiness());
		JsonNode params=JSON.parseJSONTree(message.getParams());
		messageBusiness.getChannel().forEach((key,value)->{
			boolean isTemplateParam=false;
			JsonNode templateParam=params;
			String templateParamString="";
			if(!StringUtils.isEmpty(value.getTemplate().getParam())) {
				isTemplateParam=true;
				templateParamString=getReplaceString(message.getBusiness()+"."+key+".param", params);
				templateParam=JSON.parseJSONTree(templateParamString);
			}
			String title="";
			if(!StringUtils.isEmpty(value.getTemplate().getTitle())) {
				title=getReplaceString(message.getBusiness()+"."+key+".title", templateParam);
			}
			String content="";
			if(!StringUtils.isEmpty(value.getTemplate().getText())) {
				content=getReplaceString(message.getBusiness()+"."+key+".text", templateParam);
			}
			MessageVO messageVo=new MessageVO();
			messageVo.setMessageContent(content);
			messageVo.setTitle(title);
			messageVo.setParams(isTemplateParam?templateParamString:message.getParams());
			messageVo.setMessageType(key);
			messageVo.setStatus(Integer.parseInt(MessageStatus.UNREAD.getValue()));
			try {
				User  user = SecurityContextHelper.getCurrentUser();
				messageVo.setUserName(user == null ? "系统" : user.getFullName());
			}catch (Exception e){
				e.printStackTrace();
			}
			super.create(messageVo);
			
			for (MessageReceive receive : message.getReceives()) {
				log.info("createMessage  foreach param {}",JSON.toJSONString(receive));
				MessageDetailsVO MessageDetailsVO=new MessageDetailsVO();
				MessageDetailsVO.setMessageId(messageVo.getId());
				ChannelMessage channelMessage=new ChannelMessage(key, messageVo.getParams(), null, value.getParameters(),null);
				switch (receive.getReceiveType()) {
				case DIRECT:
					channelMessage.setReceive(receive.getReceiver().toString());
					MessageDetailsVO.setReceiverType("0");
					break;
				case USER:
					User user = JSON.parse(JSON.toJSONString(receive.getReceiver()), User.class);
					MessageDetailsVO.setUserId(user.getId());
					MessageDetailsVO.setUserName(user.getFullName());
					MessageDetailsVO.setReceiverType("1");
					channelMessage.setReceive(channelHandler.getReceive(user,key));
					break;
				}
				MessageDetailsVO.setReceiver(channelMessage.getReceive());
				MessageDetailsVO.setStatus(Integer.parseInt(MessageStatus.UNREAD.getValue()));
				messageDetailService.create(MessageDetailsVO);
				channelMessage.setId(MessageDetailsVO.getId());
				messageVo.addReceive(MessageDetailsVO);
				try {
				channelHandler.write(channelMessage);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			messages.add(messageVo);
		});
		return messages;
	}
	
	private String getReplaceString(String template,JsonNode node) {
		StringWriter textWrite=new StringWriter();
		try {
			freemarkerTemplateConfiguration.getTemplate(template).process(node, textWrite);
			return textWrite.toString();
		} catch (TemplateException | IOException e) {
			e.printStackTrace();
			throw new SystemException("消息生成失败",MessageExceptionMessges.TEMPLATE_GENERATOR_ERROR);
		}finally {
			try {
				textWrite.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	

	@Override
	public Page<MessageVO> iipage(QueryBuilder builder) {
		// 1.查询登录用户 消息类型是站内信 且 接收人是登录账户的userid 或 手机号（之前很多代码都是根据手机号发送消息）
		// 以条件1代替数据权限
		builder.and(MessageMapping.USERID, SecurityContextHelper.getCurrentUserId()).or(MessageMapping.RECEIVER,QueryOperator.EQ,SecurityContextHelper.getCurrentUser().getPhone());
		builder.and(MessageMapping.MESSAGETYPE,QueryOperator.EQ,com.cloudok.base.message.enums.MessageType.II.getValue());
		List<MessageVO> list = convert2VO(messageDao.iilist(builder));
		Page<MessageVO> page=new Page<>();
		page.setData(list);
		page.setPageNo(builder.getPageCondition().getPageNo());
		page.setPageSize(builder.getPageCondition().getPageSize());
		page.setTotalCount(CollectionUtils.isEmpty(list)?0:messageDao.iicount(builder.excludeSortPage()));
		return page;
	}

	@Override
	public void read(List<Long> ids) {
//		messageDao.read(new ReadMessage(SecurityContextHelper.getCurrentUserId(), ids));
		messageDao.readDetail(new ReadMessage(SecurityContextHelper.getCurrentUserId(), ids));
	}

}
