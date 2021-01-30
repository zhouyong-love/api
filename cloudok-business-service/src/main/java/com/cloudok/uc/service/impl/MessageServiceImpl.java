package com.cloudok.uc.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cloudok.core.event.BusinessEvent;
import com.cloudok.core.exception.CoreExceptionMessage;
import com.cloudok.core.exception.SystemException;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.query.QueryOperator;
import com.cloudok.core.service.AbstractService;
import com.cloudok.core.vo.Page;
import com.cloudok.enums.UCMessageType;
import com.cloudok.security.SecurityContextHelper;
import com.cloudok.uc.dto.SimpleMemberInfo;
import com.cloudok.uc.dto.WholeMemberDTO;
import com.cloudok.uc.event.RecognizedCreateEvent;
import com.cloudok.uc.event.RecognizedDeleteEvent;
import com.cloudok.uc.mapper.MessageMapper;
import com.cloudok.uc.mapping.MessageMapping;
import com.cloudok.uc.po.MessagePO;
import com.cloudok.uc.service.MemberService;
import com.cloudok.uc.service.MessageService;
import com.cloudok.uc.vo.MessageThreadVO;
import com.cloudok.uc.vo.MessageVO;
import com.cloudok.uc.vo.RecognizedVO;


@Service("UCMessageServiceImpl")
public class MessageServiceImpl extends AbstractService<MessageVO, MessagePO> implements MessageService, ApplicationListener<BusinessEvent<?>>{

	@Autowired
	private MemberService memberService;
	
	@Autowired
	private MessageMapper repository;
	
	@Autowired
	public MessageServiceImpl(MessageMapper repository) {
		super(repository);
	}
	
	@Override
	public MessageVO convert2VO(MessagePO e) {
		MessageVO vo =  super.convert2VO(e);
		vo.setFrom(new SimpleMemberInfo(e.getFromId()));
		vo.setTo(new SimpleMemberInfo(e.getToId()));
		return vo;
	}
	@Override
	public MessagePO convert2PO(MessageVO d) {
		MessagePO p =  super.convert2PO(d);
		p.setFromId(d.getFrom().getId());
		p.setToId(d.getTo().getId());
		return p;
	}
	
	@Override
	public MessageVO createByMember(@Valid MessageVO vo) {
		if(vo.getType().toString().equals(UCMessageType.recognized.getValue())) {
			throw new SystemException(CoreExceptionMessage.NO_PERMISSION);
		}
		if(vo.getType().toString().equals(UCMessageType.privateMessage.getValue())) {//私聊
			vo.setThreadId(getOrGeneratorThreadId(vo.getTo().getId(),SecurityContextHelper.getCurrentUserId()));
		}else {
			if(vo.getThreadId() == null) {
				vo.setTenantId(this.getPrimaryKey());
			}
		}
		vo.setFrom(new SimpleMemberInfo(SecurityContextHelper.getCurrentUserId()));
		return this.create(vo);
	}
	
	@Override
	public MessageVO createByRecognized(@Valid MessageVO vo) {
		vo.setFrom(new SimpleMemberInfo(SecurityContextHelper.getCurrentUserId()));
		vo.setThreadId(getOrGeneratorThreadId(vo.getTo().getId(),SecurityContextHelper.getCurrentUserId()));
		return this.create(vo);
	}
	
	private Long getOrGeneratorThreadId(Long from,Long to) { //两个人id之和算一个会好id，省事，好用，且对于某一个确定的用户来说唯一
		return from+to;
	}
	
	@Override
	public List<MessageVO> convert2VO(List<MessagePO> e) {
		List<MessageVO> list =  super.convert2VO(e);
		if(CollectionUtils.isEmpty(list)) {
			List<Long> fromIdList = list.stream().map(item -> item.getFrom().getId()).distinct().collect(Collectors.toList());
			List<Long> toIdList = list.stream().map(item -> item.getTo().getId()).distinct().collect(Collectors.toList());
			List<Long> idList = new ArrayList<Long>();
			idList.addAll(fromIdList);
			idList.addAll(toIdList);
			idList = idList.stream().distinct().collect(Collectors.toList());
			List<SimpleMemberInfo> simpleList =  memberService.getSimpleMemberInfo(idList);
			list.stream().filter(item -> !item.getType().toString().equals(UCMessageType.privateInteraction.getValue())).forEach(item -> {
				simpleList.stream()
				.filter(mem -> mem.getId().equals(item.getFrom().getId())).findAny().ifPresent(mem -> {
					item.setFrom(mem);
				});
				simpleList.stream().filter(mem -> mem.getId().equals(item.getTo().getId())).findAny().ifPresent(mem -> {
					item.setTo(mem);
				});
			});
		}
		return list;
	}

	
	@Override
	public MessageVO updateByMember(@Valid MessageVO d) {
		MessageVO vo = this.get(d.getId());
		if (vo != null) {
			if (!vo.getFrom().getId().equals(SecurityContextHelper.getCurrentUserId())) {
				throw new SystemException(CoreExceptionMessage.NO_PERMISSION);
			}
		}
		return null;
	}

	@Override
	public Integer removeByMember(Long id) {
		MessageVO vo = this.get(id);
		if (vo != null) {
			if (!vo.getFrom().getId().equals(SecurityContextHelper.getCurrentUserId())
				|| ! ( vo.getTo().getId().equals(SecurityContextHelper.getCurrentUserId()) 
						&&
						( vo.getType().toString().equals(UCMessageType.privateInteraction.getValue())
						||
						vo.getType().toString().equals(UCMessageType.publicInteraction.getValue()) )
						
					) ){
				throw new SystemException(CoreExceptionMessage.NO_PERMISSION);
			}
		}
		return this.remove(id);
	}
	@Override
	public Page<MessageThreadVO> searchInteractionMessages(Long memberId, Integer pageNo, Integer pageSize) {
		Page<MessageThreadVO> page=new Page<>();
		page.setTotalCount(repository.searchInteractionMessagesCount(memberId));
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		if (page.getTotalCount() > 0 && (page.getTotalCount() / pageSize + 1) >= pageNo) {
			page.setData(this.getMessageThread(repository.searchInteractionMessages(memberId,(pageNo-1)*pageSize,pageNo*pageSize),2));
		}
		return page;
	}
	private List<MessageThreadVO> getMessageThread(List<Long> threadIdList,int limit){
		List<MessageThreadVO> result = new ArrayList<MessageThreadVO>();
		//预取 threadIdList.size * limit * 5 条  判断是否所有的thread都满足了limit条数，无限递归处理
		List<MessagePO>  list = this.repository.select(QueryBuilder.create(MessageMapping.class).and(MessageMapping.THREADID, QueryOperator.IN,threadIdList).end()
				.sort(MessageMapping.ID).desc().enablePaging().page(0, threadIdList.size()*2*limit).end());
		if(CollectionUtils.isEmpty(list)) {
			return result;
		}else {
			Map<Long,List<MessagePO>> map = list.stream().collect(Collectors.groupingBy(MessagePO::getThreadId));
			List<Long> notMatchedthreadIdList = new ArrayList<Long>();
			List<MessagePO> matchedMessgeList = new ArrayList<MessagePO>();
			map.forEach((key,value)->{
				if(value.size()>=limit) {
					matchedMessgeList.addAll(value.subList(0, limit));
				}else {
					notMatchedthreadIdList.add(key);
				}
			});
			this.convert2VO(matchedMessgeList).stream().collect(Collectors.groupingBy(MessageVO::getThreadId)).forEach((key,value)->{
				result.add(new MessageThreadVO(key, value));
			});
			if(!CollectionUtils.isEmpty(notMatchedthreadIdList)) {
				result.addAll(this.getMessageThread(notMatchedthreadIdList, limit)); //递归查剩下的
			}
		}
		return result;
	}
	@Override
	public MessageThreadVO getByThreadId(Long id) {
		MessageThreadVO vo = new MessageThreadVO();
		List<MessageVO> messageList = this.list(QueryBuilder.create(MessageMapping.class).and(MessageMapping.THREADID, id).end().sort(MessageMapping.ID).desc());
		vo.setThreadId(id);
		//防止数据越权 查看了私聊信息
		if(!CollectionUtils.isEmpty(messageList)) {
			messageList.stream().filter(item -> item.getType().toString().equals(UCMessageType.privateMessage.getValue())
					|| 
					 item.getType().toString().equals(UCMessageType.recognized.getValue()))
			.filter(item -> 
					!(item.getFrom().getId().equals(SecurityContextHelper.getCurrentUserId())
					|| item.getTo().getId().equals(SecurityContextHelper.getCurrentUserId())
				)).findAny().ifPresent(item -> {
					throw new SystemException(CoreExceptionMessage.NO_PERMISSION);
				});
		}
		vo.setMessageList(messageList);
		return vo;
	}
	@Override
	public Page<MessageThreadVO> searchPrivateMessages(Long memberId, Integer pageNo, Integer pageSize) {
		Page<MessageThreadVO> page=new Page<>();
		page.setTotalCount(repository.searchPrivateMessagesCount(memberId));
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		if (page.getTotalCount() > 0 && (page.getTotalCount() / pageSize + 1) >= pageNo) {
			page.setData(this.getMessageThread(repository.searchPrivateMessages(memberId,(pageNo-1)*pageSize,pageNo*pageSize),2));
		}
		return page;
	}

	@Override
	public void onApplicationEvent(BusinessEvent<?> arg0) {
		if(arg0 instanceof RecognizedCreateEvent) {
			RecognizedCreateEvent event = RecognizedCreateEvent.class.cast(arg0);
			RecognizedVO vo = event.getEventData();
			MessageVO message = new MessageVO();
			message.setContent("认可消息");
			message.setFrom(new SimpleMemberInfo(vo.getSourceId()));
			message.setTo(new SimpleMemberInfo(vo.getTargetId()));
			this.createByRecognized(message);
		}else if(arg0 instanceof RecognizedDeleteEvent) {
			RecognizedDeleteEvent event = RecognizedDeleteEvent.class.cast(arg0);
			RecognizedVO vo = event.getEventData();
			List<MessagePO> list = this.repository.select(QueryBuilder.create(MessageMapping.class).and(MessageMapping.FROMID, vo.getSourceId()).and(MessageMapping.TOID, vo.getTargetId())
					.and(MessageMapping.TYPE, UCMessageType.recognized.getValue()).end());
			if(!CollectionUtils.isEmpty(list)) {
				this.remove(list.stream().map(item -> item.getId()).collect(Collectors.toList()));
			}
			
		}
		
	}
}
