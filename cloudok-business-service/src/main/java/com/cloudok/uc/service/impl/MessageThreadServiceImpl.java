package com.cloudok.uc.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.cloudok.core.context.SpringApplicationContext;
import com.cloudok.core.event.BusinessEvent;
import com.cloudok.core.exception.CoreExceptionMessage;
import com.cloudok.core.exception.SystemException;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.query.QueryOperator;
import com.cloudok.core.service.AbstractService;
import com.cloudok.core.vo.Page;
import com.cloudok.enums.UCMessageThreadType;
import com.cloudok.enums.UCMessageType;
import com.cloudok.security.SecurityContextHelper;
import com.cloudok.uc.dto.SimpleMemberInfo;
import com.cloudok.uc.event.MessageSendEvent;
import com.cloudok.uc.event.RecognizedCreateEvent;
import com.cloudok.uc.event.RecognizedDeleteEvent;
import com.cloudok.uc.mapper.MessageThreadMapper;
import com.cloudok.uc.mapping.MessageMapping;
import com.cloudok.uc.mapping.MessageThreadMembersMapping;
import com.cloudok.uc.mapping.RecognizedMapping;
import com.cloudok.uc.po.MessageThreadPO;
import com.cloudok.uc.po.UnReadCount;
import com.cloudok.uc.service.FirendService;
import com.cloudok.uc.service.MemberService;
import com.cloudok.uc.service.MessageService;
import com.cloudok.uc.service.MessageThreadMembersService;
import com.cloudok.uc.service.MessageThreadService;
import com.cloudok.uc.service.RecognizedService;
import com.cloudok.uc.vo.MessageThreadMembersVO;
import com.cloudok.uc.vo.MessageThreadVO;
import com.cloudok.uc.vo.MessageVO;
import com.cloudok.uc.vo.RecognizedVO;

@Service
public class MessageThreadServiceImpl extends AbstractService<MessageThreadVO, MessageThreadPO> implements MessageThreadService,ApplicationListener<BusinessEvent<?>>{

	@Autowired
	private RecognizedService recognizedService;
	
	@Autowired
	private MessageThreadMapper repository;
	
	@Autowired
	private MessageThreadMembersService messageThreadMembersService;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private FirendService firendService;
	
	@Autowired
	public MessageThreadServiceImpl(MessageThreadMapper repository) {
		super(repository);
	}
	
	
	private MessageThreadVO createInteractionThread(Long ownerId,List<Long> memberIdList) {
		MessageThreadVO thread = new MessageThreadVO();
		thread.setIsPublic(false);
		thread.setOwnerId(ownerId);
		thread.setType(UCMessageThreadType.interaction.getValue());
		this.create(thread);
		List<MessageThreadMembersVO> list =	memberIdList.stream().map(item -> {
			MessageThreadMembersVO v = new MessageThreadMembersVO();
			v.setThreadId(thread.getId());
			v.setMemberId(item);
			return v;
		}).collect(Collectors.toList());
		messageThreadMembersService.create(list);
		return thread;
	}
	 
	
	private MessageThreadVO getOrCreateChatThread(List<Long> memberIdList) {
		MessageThreadPO db = repository.getMessageThreadByMemebrs(memberIdList, UCMessageThreadType.chat.getValue());
		if(db == null) {
			MessageThreadVO thread = new MessageThreadVO();
			thread.setIsPublic(false);
			thread.setType(UCMessageThreadType.chat.getValue());
			this.create(thread);
			List<MessageThreadMembersVO> list =	memberIdList.stream().map(item -> {
				MessageThreadMembersVO v = new MessageThreadMembersVO();
				v.setThreadId(thread.getId());
				v.setMemberId(item);
				return v;
			}).collect(Collectors.toList());
			messageThreadMembersService.create(list);
			return thread;
		}else {
			return this.convert2VO(db);
		}
	}
	 
	private void sendMessage(Long threadId, Long fromId, String messageType, String content) {
		MessageVO message = new MessageVO();
		message.setThreadId(threadId);
		message.setContent(content);
		message.setMemberId(fromId);
		message.setType(messageType);
		messageService.create(message);
		//发布事件
		SpringApplicationContext.publishEvent(new MessageSendEvent(message));
	}	
	
	private void onRecognized(RecognizedVO recognized) {
		//认可某人，如果相互认可了，则相互加入到好友表
		List<Long> memberIdList = Arrays.asList(recognized.getTargetId(),recognized.getSourceId());
		RecognizedVO sourceRecognized = this.recognizedService.get(QueryBuilder.create(RecognizedMapping.class)
				.and(RecognizedMapping.SOURCEID, recognized.getTargetId())
				.and(RecognizedMapping.TARGETID, recognized.getSourceId()).end());
		if(sourceRecognized != null) { //表示相互认可了 source 认可了 target 且 target 认可了 source
			MessageThreadVO thread = this.getOrCreateChatThread(memberIdList);
			this.sendMessage(thread.getId(),recognized.getSourceId(), UCMessageType.recognized.getValue(), "成为了新的Peer！发条私信吧");
		}
		
	}

	private void onUnRecognized(RecognizedVO recognized) {
		List<Long> memberIdList = Arrays.asList(recognized.getTargetId(),recognized.getSourceId());
		MessageThreadPO thread = repository.getMessageThreadByMemebrs(memberIdList, UCMessageThreadType.chat.getValue());
		if(thread != null) { //删除私信对话组
			this.remove(thread.getId());
		}
	}
	 
	private MessageThreadVO getBaseInfo(Long threadId){
		List<MessageThreadVO>  threadList =this.getBaseInfo(Arrays.asList(threadId));
		return CollectionUtils.isEmpty(threadList) ? null : threadList.get(0);
	}
	private List<MessageThreadVO> getBaseInfo(List<Long> messageThreadList){
		if(CollectionUtils.isEmpty(messageThreadList)) {
			return Collections.emptyList();
		}
		List<Long> threadIdList = messageThreadList.stream().distinct().collect(Collectors.toList());
		List<MessageThreadVO> list = this.get(threadIdList);
		this.fillThreadInfo(list);
		return list;
	}

	//	type 1 认可消息 2 私信 3 留言 4 留言公开回复 5 留言私密回复
	@Override
	public MessageThreadVO createByMember(@Valid MessageVO vo) {
		if(vo.getToMemberId().equals(getCurrentUserId())) {
			throw new SystemException("不能给自己发送消息",CoreExceptionMessage.PARAMETER_ERR);
		}
		if(StringUtils.isEmpty(vo.getType())) {
			throw new SystemException("消息类型不能为空",CoreExceptionMessage.PARAMETER_ERR);
		}
		if(UCMessageType.recognized.getValue().equals(vo.getType())) { //认可
			throw new SystemException("消息类型不能为认可消息",CoreExceptionMessage.PARAMETER_ERR);
		}
		MessageThreadVO thread = null;
		//thread不存在，创建新的
		if(vo.getThreadId() == null || vo.getThreadId().equals(-1L)) {
			if(!UCMessageType.interaction.getValue().equals(vo.getType()) ) { //非留言，不行有threadId
				throw new SystemException("ThreadId不能为空",CoreExceptionMessage.PARAMETER_ERR);
			}
			thread = this.createInteractionThread(vo.getToMemberId(),Arrays.asList(vo.getMemberId(),vo.getToMemberId()));
			//填充下基本信息
			thread = this.getBaseInfo(thread.getId());
		}else {
			//检查thread权限
			thread = this.getBaseInfo(vo.getThreadId());
		}
		//检查是否有权限
		if(!thread.getMemberList().stream().map(item -> item.getId())
				.filter(item -> item.equals(SecurityContextHelper.getCurrentUserId())).findAny().isPresent()) {
			throw new SystemException("您没有权限参与",CoreExceptionMessage.NO_PERMISSION);
		}
		
		this.sendMessage(thread.getId(), SecurityContextHelper.getCurrentUserId(), vo.getType(), vo.getContent());
		
		if(vo.getType().equals(UCMessageType.pubicInteractionReply.getValue())) {
			MessageThreadVO merge = new MessageThreadVO();
			merge.setId(thread.getId());
			merge.setIsPublic(true);
			this.merge(merge);
			thread.setIsPublic(true);
		}
		return thread;
	}


	@Override
	public Page<MessageVO> getMessageByThreadId(Long threadId, Integer pageNo, Integer pageSize) {
		MessageThreadVO thread =  this.getBaseInfo(threadId);
		//检查是否有权限
		if(thread.getIsPublic() == false && !thread.getMemberList().stream().map(item -> item.getId())
				.filter(item -> item.equals(SecurityContextHelper.getCurrentUserId())).findAny().isPresent()) {
			throw new SystemException(CoreExceptionMessage.NO_PERMISSION);
		}
		//查询消息
		Page<MessageVO>  page = this.messageService.page(QueryBuilder.create(MessageMapping.class)
				.and(MessageMapping.THREADID, threadId).end()
				.sort(MessageMapping.CREATETIME).desc().enablePaging().page(pageNo, pageSize).end());
		
		return page;
	}


	@Override
	public Page<MessageThreadVO> searchChatMessageThreads(Long memberId, Integer pageNo, Integer pageSize) {
		Page<MessageThreadVO> page=new Page<>();
		page.setTotalCount(repository.searchChatMessageThreadsCount(memberId));
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		if (page.getTotalCount() > 0 && (page.getTotalCount() / pageSize + 1) >= pageNo) {
			List<MessageThreadVO> list = this.getMessageThread(repository.searchChatMessageThreads(memberId,(pageNo-1)*pageSize,pageNo*pageSize),2);
			List<UnReadCount> countList = this.repository.getUnReadMessages(memberId,list.stream().map(item -> item.getId()).distinct().collect(Collectors.toList()));
			if(!CollectionUtils.isEmpty(countList)&&!CollectionUtils.isEmpty(list)) {
				list.stream().forEach(item ->{
					countList.stream().filter( a -> item.getId().equals(a.getThreadId())).findAny().ifPresent(a ->{
						item.setUnReadCount(a.getCount());
					});
				});
			}
			this.fillThreadInfo(list);
			page.setData(list);
		}
		return page;
	}
	
	private List<MessageThreadVO> getMessageThread(List<MessageThreadPO> threadList,int limit){
		if(CollectionUtils.isEmpty(threadList)) {
			return Collections.emptyList();
		}
		List<MessageThreadVO> list =  this.getMessageThreadByIdList(threadList.stream().map(item -> item.getId()).distinct().collect(Collectors.toList()), limit);
		//递归造成了数据排序混乱，回复排序
		return threadList.stream().map(item -> {
			Optional<MessageThreadVO>  opt = list.stream().filter( t -> t.getId().equals(item.getId())).findAny();
			if(opt.isPresent()) {
				MessageThreadVO message = opt.get();
				message.setLastUpdate(item.getUpdateTs());
				return message;
			}
			return null;
		}).filter(item -> item != null).collect(Collectors.toList());
	}
	
	private List<MessageThreadVO> getMessageThreadByIdList(List<Long> threadIdList,int limit){
		List<MessageThreadVO> result = new ArrayList<MessageThreadVO>();
		//预取 threadIdList.size * limit * 5 条  判断是否所有的thread都满足了limit条数，无限递归处理
		List<MessageVO>  list = this.messageService.list(QueryBuilder.create(MessageMapping.class).and(MessageMapping.THREADID, QueryOperator.IN,threadIdList).end()
				.sort(MessageMapping.ID).desc().enablePaging().page(0, threadIdList.size()*10*limit).end());
		Map<Long,List<MessageVO>> map = list.stream().collect(Collectors.groupingBy(MessageVO::getThreadId));
		List<Long> notMatchedthreadIdList = new ArrayList<Long>();
		List<MessageVO> matchedMessgeList = new ArrayList<MessageVO>();
		List<MessageVO> notMatchedMessgeList = new ArrayList<MessageVO>();
		map.forEach((key,value)->{
			if(value.size()>=limit) {
				matchedMessgeList.addAll(value.subList(0, limit));
			}else {
				notMatchedMessgeList.addAll(value);
				notMatchedthreadIdList.add(key);
			}
		});
		matchedMessgeList.stream().collect(Collectors.groupingBy(MessageVO::getThreadId)).forEach((key,value)->{
			result.add(new MessageThreadVO(key, value.stream().sorted((a,b)->a.getId().compareTo(b.getId())).collect(Collectors.toList())));
		});
		if(!CollectionUtils.isEmpty(notMatchedthreadIdList)) {
			//判断是否要递归查询
			if(list.size() >= threadIdList.size()*limit) { //数据量小于最小数据要求，则递归查
				result.addAll(this.getMessageThreadByIdList(notMatchedthreadIdList, limit)); //递归查剩下的
			}else { //不递归，就捞出没有加进去的数据
				notMatchedMessgeList.stream().collect(Collectors.groupingBy(MessageVO::getThreadId))
				.forEach((key,value)->{
					result.add(new MessageThreadVO(key, value));
				});
			}
		}
		return result;
	}


	@Override
	public Page<MessageThreadVO> searchInteractionMessageThreads(Long memberId, Integer status, Integer pageNo,
			Integer pageSize) {
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
		page.setTotalCount(repository.searchInteractionMessageThreadsCount(memberId,getCurrentUserId(),status,seeOthers));
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		if (page.getTotalCount() > 0 && (page.getTotalCount() / pageSize + 1) >= pageNo) {
			List<MessageThreadVO> dataList = this.getMessageThread(repository.searchInteractionMessageThreads(memberId,getCurrentUserId(),status,seeOthers,(pageNo-1)*pageSize,pageNo*pageSize),2);
			this.fillThreadInfo(dataList);
			page.setData(dataList);
		}
		return page;
	}


//	 viewType=1 我收到的 viewType=2 回复我的
	@Override
	public Page<MessageThreadVO> searchMyInteractionMessageThreads(Integer viewType, Integer pageNo, Integer pageSize) {
		//看自己的，查询所有相关数据就可以
		//如果是看别人的，则 要求必须已经公开回复的 或者  回复给自己的
		Page<MessageThreadVO> page=new Page<>();
		Long memberId = SecurityContextHelper.getCurrentUserId();
		page.setTotalCount(repository.searchMyInteractionMessageThreadsCount(memberId,viewType));
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		if (page.getTotalCount() > 0 && (page.getTotalCount() / pageSize + 1) >= pageNo) {
			List<MessageThreadVO> dataList = this.getMessageThread(repository.searchMyInteractionMessageThreads(memberId,viewType,(pageNo-1)*pageSize,pageNo*pageSize),2);
			this.fillThreadInfo(dataList);
			page.setData(dataList);
		}
		return page;
	}
	
	private void fillThreadInfo(List<MessageThreadVO> list) {
		if(!CollectionUtils.isEmpty(list)) {
			List<Long> threadIdList = list.stream().map(item -> item.getId()).distinct().collect(Collectors.toList());
			List<MessageThreadMembersVO> tempList = this.messageThreadMembersService.list(QueryBuilder.create(MessageThreadMembersMapping.class)
					.and(MessageThreadMembersMapping.THREADID, QueryOperator.IN,threadIdList).end());
			if(!CollectionUtils.isEmpty(tempList)) {
				List<Long> memberIdList = tempList.stream().map(item -> item.getMemberId()).distinct().collect(Collectors.toList());
				List<SimpleMemberInfo> simpleMemberInfoList = memberService.getSimpleMemberInfo(memberIdList);
				Map<Long,List<MessageThreadMembersVO>> map =  tempList.stream().collect(Collectors.groupingBy(MessageThreadMembersVO::getThreadId));
				list.stream().forEach(item ->{
					List<MessageThreadMembersVO> memberList = map.get(item.getId());
					if(!CollectionUtils.isEmpty(memberList)) {
						item.setMemberList(memberList.stream().map( m ->{
						Optional<SimpleMemberInfo> opt =	simpleMemberInfoList.stream().filter(a -> a.getId().equals(m.getMemberId())).findAny();
						return opt.isPresent() ? opt.get() : null;
						}).distinct().collect(Collectors.toList()));
					}
				});
			}
		}
	}
	
	@Override
	public MessageThreadVO getMessageThreadByMemberId(Long currentUserId, Long memberId, Integer latestMessageCount) {
		//检查是否好友
		if(!this.firendService.isFirends(currentUserId,memberId)) {
			throw new SystemException("只有相互认可后才能发送私信",CoreExceptionMessage.NO_PERMISSION);
		}
		
		MessageThreadVO thread = this.getOrCreateChatThread(Arrays.asList(currentUserId,memberId));
		thread = this.getBaseInfo(thread.getId());
		//最多取10条
		if(latestMessageCount == null) {
			latestMessageCount = 10;
		}
		if(latestMessageCount>10) {
			latestMessageCount = 10;
		}
		Page<MessageVO>  page = this.messageService.page(QueryBuilder.create(MessageMapping.class)
				.and(MessageMapping.THREADID, thread.getId()).end()
				.sort(MessageMapping.CREATETIME).desc().enablePaging().page(1, latestMessageCount).end());
		thread.setLatestMessageList(page.getData());
		return thread;
	}
	


	@Override
	public void onApplicationEvent(BusinessEvent<?> arg0) {
		if(arg0 instanceof RecognizedCreateEvent) {
			this.onRecognized(RecognizedCreateEvent.class.cast(arg0).getEventData());
		}else if(arg0 instanceof RecognizedDeleteEvent) {
			this.onUnRecognized(RecognizedDeleteEvent.class.cast(arg0).getEventData());
		}else if(arg0 instanceof MessageSendEvent) {
			this.onMessageSendEvent(MessageSendEvent.class.cast(arg0).getEventData());
		}
	}


	private void onMessageSendEvent(MessageVO eventData) {
		MessageThreadVO vo = new MessageThreadVO();
		vo.setId(eventData.getThreadId());
		vo.setLastMessageId(eventData.getId());
		this.merge(vo);
	}


	@Override
	public void readed(Long messageId) {
		MessageVO message = messageService.get(messageId);
		MessageThreadMembersVO mtmv = new MessageThreadMembersVO();
		mtmv.setId(messageThreadMembersService.list(QueryBuilder.create(MessageThreadMembersMapping.class).and(MessageThreadMembersMapping.THREADID, message.getThreadId()).and(MessageThreadMembersMapping.MEMBERID, getCurrentUserId()).end())
				.get(0).getId());
		mtmv.setLastPosition(messageId);
		messageThreadMembersService.merge(mtmv);
	}



}
