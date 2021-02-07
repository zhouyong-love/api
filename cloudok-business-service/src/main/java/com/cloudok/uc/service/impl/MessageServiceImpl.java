package com.cloudok.uc.service.impl;

import java.sql.Timestamp;
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
import com.cloudok.uc.event.RecognizedCreateEvent;
import com.cloudok.uc.event.RecognizedDeleteEvent;
import com.cloudok.uc.mapper.MessageMapper;
import com.cloudok.uc.mapper.RecognizedMapper;
import com.cloudok.uc.mapping.MessageMapping;
import com.cloudok.uc.mapping.RecognizedMapping;
import com.cloudok.uc.po.MessagePO;
import com.cloudok.uc.po.UnReadCount;
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
		if(d.getFrom()!=null)
		p.setFromId(d.getFrom().getId());
		if(d.getTo()!=null)
		p.setToId(d.getTo().getId());
		return p;
	}
	@Override
	public MessageVO create(MessageVO d) {
		d.setStatus(0);
		d.setStatusTs(new Timestamp(System.currentTimeMillis()));
		return super.create(d);
	}
	
	@Override
	public MessageVO createByMember(@Valid MessageVO vo) {
		if("-1".equals(vo.getThreadId())) {
			vo.setThreadId(null);
		}
		if(vo.getType().toString().equals(UCMessageType.recognized.getValue())) {
			throw new SystemException(CoreExceptionMessage.NO_PERMISSION);
		}
		if(vo.getType().toString().equals(UCMessageType.privateMessage.getValue())) {//私聊
			vo.setThreadId(getOrGeneratorThreadId(vo.getTo().getId(),SecurityContextHelper.getCurrentUserId()));
		}else {
			if(vo.getThreadId() == null) { //留言
				vo.setThreadId(vo.getTo().getId()+"-"+this.getPrimaryKey());
				vo.setType(Integer.parseInt(UCMessageType.interaction.getValue())); //留言
			}
		}
		vo.setFrom(new SimpleMemberInfo(SecurityContextHelper.getCurrentUserId()));
		if(vo.getThreadId()!=null) {
			this.list(QueryBuilder.create(MessageMapping.class).and(MessageMapping.THREADID, vo.getThreadId()).and(MessageMapping.STATUS, 0).end())
			.forEach(item->{
				this.merge(new MessageVO(item.getId(),1));
			});
		}
		return this.create(vo);
	}
	
	@Override
	public MessageVO createByRecognized(@Valid MessageVO vo) {
		vo.setFrom(new SimpleMemberInfo(SecurityContextHelper.getCurrentUserId()));
		vo.setThreadId(getOrGeneratorThreadId(vo.getTo().getId(),SecurityContextHelper.getCurrentUserId()));
		return this.create(vo);
	}
	
	private String getOrGeneratorThreadId(Long from,Long to) { //两个人id之和算一个会好id，省事，好用，且对于某一个确定的用户来说唯一
		return String.valueOf(from+to);
	}
	
	@Override
	public List<MessageVO> convert2VO(List<MessagePO> e) {
		List<MessageVO> list =  super.convert2VO(e);
		if(!CollectionUtils.isEmpty(list)) {
			List<Long> fromIdList = list.stream().map(item -> item.getFrom().getId()).distinct().collect(Collectors.toList());
			List<Long> toIdList = list.stream().map(item -> item.getTo().getId()).distinct().collect(Collectors.toList());
			List<Long> idList = new ArrayList<Long>();
			idList.addAll(fromIdList);
			idList.addAll(toIdList);
			idList = idList.stream().distinct().collect(Collectors.toList());
			List<SimpleMemberInfo> simpleList =  memberService.getSimpleMemberInfo(idList);
			list.stream()
			.forEach(item -> {
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
	public Page<MessageThreadVO> searchMyInteractionMessages(Integer viewType, Integer pageNo, Integer pageSize) {
		Long memberId = getCurrentUserId();
		//看自己的，查询所有相关数据就可以
		//如果是看别人的，则 要求必须已经公开回复的 或者  回复给自己的
		Page<MessageThreadVO> page=new Page<>();
		page.setTotalCount(repository.searchMyInteractionMessagesCount(memberId,viewType));
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		if (page.getTotalCount() > 0 && (page.getTotalCount() / pageSize + 1) >= pageNo) {
			List<MessageThreadVO> dataList = this.getMessageThread(repository.searchMyInteractionMessages(memberId,viewType,(pageNo-1)*pageSize,pageNo*pageSize),2);
			if(!CollectionUtils.isEmpty(dataList)) {
				//私密互动，只有回复人和留言人可见
				dataList.stream().forEach( thread ->{
					List<MessageVO> messageList = 	thread.getMessageList();
					messageList  = messageList.stream().filter(item ->{
						if(item.getType().toString().equals(UCMessageType.privateInteraction.getValue())) {
							return item.getFrom().getId().equals(SecurityContextHelper.getCurrentUserId()) || item.getTo().getId().equals(SecurityContextHelper.getCurrentUserId()) ;
						}else{
							return true;
						}
					} ).collect(Collectors.toList());
					thread.setMessageList(messageList);
				});
			}
			page.setData(dataList);
		}
		return page;
	}
	@Override
	public Page<MessageThreadVO> searchInteractionMessages(Long memberId, Integer status,Integer pageNo, Integer pageSize) {
		Integer seeOthers = 1; //是否看自己的
		if(memberId == null) {
			memberId = getCurrentUserId();
		}
		if(memberId.equals(getCurrentUserId())) {
			seeOthers = 0; 
		}
		//看自己的，查询所有相关数据就可以
		//如果是看别人的，则 要求必须已经公开回复的 或者  回复给自己的
		
		Page<MessageThreadVO> page=new Page<>();
		page.setTotalCount(repository.searchInteractionMessagesCount(memberId,status,getCurrentUserId(),seeOthers));
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		if (page.getTotalCount() > 0 && (page.getTotalCount() / pageSize + 1) >= pageNo) {
			List<MessageThreadVO> dataList = this.getMessageThread(repository.searchInteractionMessages(memberId,status,getCurrentUserId(),seeOthers,(pageNo-1)*pageSize,pageNo*pageSize),2);
			if(!CollectionUtils.isEmpty(dataList)) {
				//私密互动，只有回复人和留言人可见
				dataList.stream().forEach( thread ->{
					List<MessageVO> messageList = 	thread.getMessageList();
					messageList  = messageList.stream().filter(item ->{
						if(item.getType().toString().equals(UCMessageType.privateInteraction.getValue())) {
							return item.getFrom().getId().equals(SecurityContextHelper.getCurrentUserId()) || item.getTo().getId().equals(SecurityContextHelper.getCurrentUserId()) ;
						}else{
							return true;
						}
					} ).collect(Collectors.toList());
					thread.setMessageList(messageList);
				});
			}
			page.setData(dataList);
		}
		return page;
	}
	private List<MessageThreadVO> getMessageThread(List<String> threadIdList,int limit){
		List<MessageThreadVO> result = new ArrayList<MessageThreadVO>();
		//预取 threadIdList.size * limit * 5 条  判断是否所有的thread都满足了limit条数，无限递归处理
		List<MessagePO>  list = this.repository.select(QueryBuilder.create(MessageMapping.class).and(MessageMapping.THREADID, QueryOperator.IN,threadIdList).end()
				.sort(MessageMapping.ID).desc().enablePaging().page(0, threadIdList.size()*2*limit).end());
		Map<String,List<MessagePO>> map = list.stream().collect(Collectors.groupingBy(MessagePO::getThreadId));
		List<String> notMatchedthreadIdList = new ArrayList<String>();
		List<MessagePO> matchedMessgeList = new ArrayList<MessagePO>();
		List<MessagePO> notMatchedMessgeList = new ArrayList<MessagePO>();
		map.forEach((key,value)->{
			if(value.size()>=limit) {
				matchedMessgeList.addAll(value.subList(0, limit));
			}else {
				notMatchedMessgeList.addAll(value);
				notMatchedthreadIdList.add(key);
			}
		});
		this.convert2VO(matchedMessgeList).stream().collect(Collectors.groupingBy(MessageVO::getThreadId)).forEach((key,value)->{
			result.add(new MessageThreadVO(key, value));
		});
		if(!CollectionUtils.isEmpty(notMatchedthreadIdList)) {
			//判断是否要递归查询
			if(list.size() >= threadIdList.size()*limit) { //数据量小于最小数据要求，则递归查
				result.addAll(this.getMessageThread(notMatchedthreadIdList, limit)); //递归查剩下的
			}else { //不递归，就捞出没有加进去的数据
				this.convert2VO(notMatchedMessgeList).stream().collect(Collectors.groupingBy(MessageVO::getThreadId))
				.forEach((key,value)->{
					result.add(new MessageThreadVO(key, value));
				});
			}
		}
		return result;
	}
	@Override
	public Page<MessageVO> getByThreadId(String id,Integer pageNo, Integer pageSize) {
		Page<MessageVO> page = this.page(QueryBuilder.create(MessageMapping.class).and(MessageMapping.THREADID, id).end()
				.sort(MessageMapping.ID).desc().enablePaging().page(pageNo, pageSize).end());
		List<MessageVO> messageList = page.getData();
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
		return page;
	}
	@Override
	public Page<MessageThreadVO> searchPrivateMessages(Long memberId, Integer pageNo, Integer pageSize) {
		Page<MessageThreadVO> page=new Page<>();
		page.setTotalCount(repository.searchPrivateMessagesCount(memberId));
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		if (page.getTotalCount() > 0 && (page.getTotalCount() / pageSize + 1) >= pageNo) {
			List<MessageThreadVO> list = this.getMessageThread(repository.searchPrivateMessages(memberId,(pageNo-1)*pageSize,pageNo*pageSize),2);
			List<UnReadCount> countList = this.repository.getUnReadMessages(memberId,list.stream().map(item -> item.getThreadId()).distinct().collect(Collectors.toList()));
			if(!CollectionUtils.isEmpty(countList)&&!CollectionUtils.isEmpty(list)) {
				list.stream().forEach(item ->{
					countList.stream().filter( a -> item.getThreadId().equals(a.getThreadId())).findAny().ifPresent(a ->{
						item.setUnReadCount(a.getCount());
					});
				});
			}
			page.setData(list);
		}
		return page;
	}

	@Autowired
	private RecognizedMapper recognizedMapper;
	
	@Override
	public void onApplicationEvent(BusinessEvent<?> arg0) {
		if(arg0 instanceof RecognizedCreateEvent) {
			RecognizedCreateEvent event = RecognizedCreateEvent.class.cast(arg0);
			RecognizedVO vo = event.getEventData();
			if(recognizedMapper.count(QueryBuilder.create(RecognizedMapping.class).and(RecognizedMapping.SOURCEID, vo.getTargetId()).and(RecognizedMapping.TARGETID, vo.getSourceId()).end())>0) {
				MessageVO message = new MessageVO();
				message.setContent("成为了新的Peers");
				message.setFrom(new SimpleMemberInfo(vo.getSourceId()));
				message.setTo(new SimpleMemberInfo(vo.getTargetId()));
				message.setType(Integer.parseInt(UCMessageType.recognized.getValue()));
				this.createByRecognized(message);
			}
		}else if(arg0 instanceof RecognizedDeleteEvent) {
			RecognizedDeleteEvent event = RecognizedDeleteEvent.class.cast(arg0);
			RecognizedVO vo = event.getEventData();
			List<MessagePO> list = this.repository.select(QueryBuilder.create(MessageMapping.class).and(MessageMapping.FROMID, vo.getSourceId()).and(MessageMapping.TOID, vo.getTargetId())
					.end().and(MessageMapping.TOID, vo.getSourceId()).and(MessageMapping.FROMID, vo.getTargetId()).end().and(MessageMapping.TYPE, UCMessageType.recognized.getValue()).end());
			if(!CollectionUtils.isEmpty(list)) {
				this.remove(list.stream().map(item -> item.getId()).collect(Collectors.toList()));
			}
			
		}
		
	}

	@Override
	public void deleteByThreadId(String threadId) {
		List<MessageVO> list = this.list(QueryBuilder.create(MessageMapping.class).and(MessageMapping.THREADID, threadId).end().enablePaging().page(1, 1).end());
		if(CollectionUtils.isEmpty(list)) {
			return;
		}
		if(list.get(0).getFrom().getId().equals(SecurityContextHelper.getCurrentUserId()) || 
		list.get(0).getTo().getId().equals(SecurityContextHelper.getCurrentUserId()) ) {
			repository.deleteByThreadId(threadId);
		}else {
			throw new SystemException(CoreExceptionMessage.NO_PERMISSION);
		}
		
	}
}
