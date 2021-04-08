package com.cloudok.uc.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
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
import com.cloudok.uc.dto.WholeMemberDTO;
import com.cloudok.uc.event.MessageSendEvent;
import com.cloudok.uc.event.RecognizedCreateEvent;
import com.cloudok.uc.event.RecognizedDeleteEvent;
import com.cloudok.uc.mapper.MessageThreadMapper;
import com.cloudok.uc.mapping.MessageMapping;
import com.cloudok.uc.mapping.MessageThreadMembersMapping;
import com.cloudok.uc.mapping.RecognizedMapping;
import com.cloudok.uc.po.MessageThreadGroupPO;
import com.cloudok.uc.po.MessageThreadPO;
import com.cloudok.uc.po.UnReadCount;
import com.cloudok.uc.service.FirendService;
import com.cloudok.uc.service.MemberService;
import com.cloudok.uc.service.MessageService;
import com.cloudok.uc.service.MessageThreadMembersService;
import com.cloudok.uc.service.MessageThreadService;
import com.cloudok.uc.service.RecognizedService;
import com.cloudok.uc.vo.MessageThreadGroup;
import com.cloudok.uc.vo.MessageThreadGroupItem;
import com.cloudok.uc.vo.MessageThreadMembersVO;
import com.cloudok.uc.vo.MessageThreadVO;
import com.cloudok.uc.vo.MessageVO;
import com.cloudok.uc.vo.RecognizedVO;

@Service
public class MessageThreadServiceImpl extends AbstractService<MessageThreadVO, MessageThreadPO> implements MessageThreadService, ApplicationListener<BusinessEvent<?>> {

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

	private MessageThreadVO createInteractionThread(Long ownerId, boolean isAnonymous, List<Long> memberIdList) {
		MessageThreadVO thread = new MessageThreadVO();
		thread.setIsPublic(false);
		thread.setOwnerId(ownerId);
		thread.setType(isAnonymous ? UCMessageThreadType.anonymousInteraction.getValue() : UCMessageThreadType.interaction.getValue());
		this.create(thread);
		List<MessageThreadMembersVO> list = memberIdList.stream().map(item -> {
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
		if (db == null) {
			MessageThreadVO thread = new MessageThreadVO();
			thread.setIsPublic(false);
			thread.setType(UCMessageThreadType.chat.getValue());
			this.create(thread);
			List<MessageThreadMembersVO> list = memberIdList.stream().map(item -> {
				MessageThreadMembersVO v = new MessageThreadMembersVO();
				v.setThreadId(thread.getId());
				v.setMemberId(item);
				return v;
			}).collect(Collectors.toList());
			messageThreadMembersService.create(list);
			return thread;
		} else {
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
		// 发布事件
		SpringApplicationContext.publishEvent(new MessageSendEvent(message));
	}

	private void onRecognized(RecognizedVO recognized) {
		// 认可某人，如果相互认可了，则相互加入到好友表
		List<Long> memberIdList = Arrays.asList(recognized.getTargetId(), recognized.getSourceId());
		RecognizedVO sourceRecognized = this.recognizedService.get(QueryBuilder.create(RecognizedMapping.class).and(RecognizedMapping.SOURCEID, recognized.getTargetId())
				.and(RecognizedMapping.TARGETID, recognized.getSourceId()).end());
		if (sourceRecognized != null) { // 表示相互认可了 source 认可了 target 且 target 认可了 source
			MessageThreadVO thread = this.getOrCreateChatThread(memberIdList);
			this.sendMessage(thread.getId(), recognized.getSourceId(), UCMessageType.recognized.getValue(), "成为了新的Peer！发条私信吧");
		}

	}

	private void onUnRecognized(RecognizedVO recognized) {
		List<Long> memberIdList = Arrays.asList(recognized.getTargetId(), recognized.getSourceId());
		MessageThreadPO thread = repository.getMessageThreadByMemebrs(memberIdList, UCMessageThreadType.chat.getValue());
		if (thread != null) { // 删除私信对话组
			this.remove(thread.getId());
		}
	}

	private MessageThreadVO getBaseInfo(Long threadId) {
		List<MessageThreadVO> threadList = this.getBaseInfo(Arrays.asList(threadId));
		return CollectionUtils.isEmpty(threadList) ? null : threadList.get(0);
	}

	private List<MessageThreadVO> getBaseInfo(List<Long> messageThreadList) {
		if (CollectionUtils.isEmpty(messageThreadList)) {
			return Collections.emptyList();
		}
		List<Long> threadIdList = messageThreadList.stream().distinct().collect(Collectors.toList());
		List<MessageThreadVO> list = this.get(threadIdList);
		this.fillThreadInfo(list, 0);
		return list;
	}

	// type 1 认可消息 2 私信 3 留言 4 留言公开回复 5 留言私密回复
	@Override
	public MessageThreadVO createByMember(@Valid MessageVO vo) {
		if (vo.getToMemberId() == null) { // 回复匿名留言才可能为空
			if (vo.getThreadId() == null || vo.getThreadId().equals(-1L)) {
				throw new SystemException("参数错误，匿名留言回复必须传入threadId", CoreExceptionMessage.PARAMETER_ERR);
			}
			List<MessageThreadMembersVO> list = this.messageThreadMembersService
					.list(QueryBuilder.create(MessageThreadMembersMapping.class).and(MessageThreadMembersMapping.THREADID, vo.getThreadId()).end());
			// 查询toMemberId
			list.stream().filter(item -> !item.getMemberId().equals(getCurrentUserId())).findAny().ifPresent(item -> {
				vo.setToMemberId(item.getMemberId());
			});
		}
		if (vo.getToMemberId().equals(getCurrentUserId())) {
			throw new SystemException("不能给自己发送消息", CoreExceptionMessage.PARAMETER_ERR);
		}
		if (StringUtils.isEmpty(vo.getType())) {
			throw new SystemException("消息类型不能为空", CoreExceptionMessage.PARAMETER_ERR);
		}
		if (UCMessageType.recognized.getValue().equals(vo.getType())) { // 认可
			throw new SystemException("消息类型不能为认可消息", CoreExceptionMessage.PARAMETER_ERR);
		}
		MessageThreadVO thread = null;
		// thread不存在，创建新的
		if (vo.getThreadId() == null || vo.getThreadId().equals(-1L)) {
			if (!UCMessageType.interaction.getValue().equals(vo.getType())) { // 非留言，不行有threadId
				throw new SystemException("ThreadId不能为空", CoreExceptionMessage.PARAMETER_ERR);
			}
			thread = this.createInteractionThread(vo.getToMemberId(), vo.isAnonymous(), Arrays.asList(vo.getMemberId(), vo.getToMemberId()));
			// 填充下基本信息
			thread = this.getBaseInfo(thread.getId());
		} else {
			// 检查thread权限
			thread = this.getBaseInfo(vo.getThreadId());
		}
		// 检查是否有权限
		if (!thread.getMemberList().stream().filter(item -> item.getId() != null).map(item -> item.getId()).filter(item -> item.equals(SecurityContextHelper.getCurrentUserId())).findAny().isPresent()) {
			throw new SystemException("您没有权限参与", CoreExceptionMessage.NO_PERMISSION);
		}

		this.sendMessage(thread.getId(), SecurityContextHelper.getCurrentUserId(), vo.getType(), vo.getContent());

		if (vo.getType().equals(UCMessageType.pubicInteractionReply.getValue())) {
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
		MessageThreadVO thread = this.getBaseInfo(threadId);
		if (!UCMessageThreadType.chat.getValue().equals(thread.getType())) {
			throw new SystemException("您没有权限参与", CoreExceptionMessage.NO_PERMISSION);
		}
		// 检查是否有权限
		if (thread.getIsPublic() == false
				&& !thread.getMemberList().stream().map(item -> item.getId()).filter(item -> item.equals(SecurityContextHelper.getCurrentUserId())).findAny().isPresent()) {
			throw new SystemException("您没有权限参与", CoreExceptionMessage.NO_PERMISSION);
		}
		// 查询消息
		Page<MessageVO> page = this.messageService.page(QueryBuilder.create(MessageMapping.class).and(MessageMapping.THREADID, threadId).end().sort(MessageMapping.CREATETIME)
				.desc().enablePaging().page(pageNo, pageSize).end());

		return page;
	}

	@Override
	public Page<MessageThreadVO> searchChatMessageThreads(Long memberId, Integer read, Integer pageNo, Integer pageSize) {
		Page<MessageThreadVO> page = new Page<>();
		page.setTotalCount(repository.searchChatMessageThreadsCount(memberId));
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		if (page.getTotalCount() > 0 && (page.getTotalCount() / pageSize + 1) >= pageNo) {
			List<MessageThreadVO> list = this.getMessageThread(repository.searchChatMessageThreads(memberId, (pageNo - 1) * pageSize, pageSize), 2);
			//List<UnReadCount> countList = this.repository.getUnReadMessages(memberId, list.stream().map(item -> item.getId()).distinct().collect(Collectors.toList()));
//			if (!CollectionUtils.isEmpty(countList) && !CollectionUtils.isEmpty(list)) {
//				list.stream().forEach(item -> {
//					countList.stream().filter(a -> item.getId().equals(a.getThreadId())).findAny().ifPresent(a -> {
//						item.setUnReadCount(a.getCount());
//					});
//				});
//			}
			this.fillThreadInfo(list, read);
			page.setData(list);
		}
		return page;
	}

	private List<MessageThreadVO> getMessageThread(List<MessageThreadPO> threadList, int limit) {
		if (CollectionUtils.isEmpty(threadList)) {
			return Collections.emptyList();
		}
		List<MessageThreadVO> list = this.getMessageThreadByIdList(threadList.stream().map(item -> item.getId()).distinct().collect(Collectors.toList()), limit);
		// 递归造成了数据排序混乱，回复排序
		return threadList.stream().map(item -> {
			Optional<MessageThreadVO> opt = list.stream().filter(t -> t.getId().equals(item.getId())).findAny();
			if (opt.isPresent()) {
				MessageThreadVO message = opt.get();
				BeanUtils.copyProperties(item, message);
				return message;
			}
			return null;
		}).filter(item -> item != null).collect(Collectors.toList());
	}

	private List<MessageThreadVO> getMessageThreadByIdList(List<Long> threadIdList, int limit) {
		List<MessageThreadVO> result = new ArrayList<MessageThreadVO>();
		// 预取 threadIdList.size * limit * 5 条 判断是否所有的thread都满足了limit条数，无限递归处理
		List<MessageVO> list = this.messageService.list(QueryBuilder.create(MessageMapping.class).and(MessageMapping.THREADID, QueryOperator.IN, threadIdList).end()
				.sort(MessageMapping.ID).desc().enablePaging().page(0, threadIdList.size() * 10 * limit).end());
		Map<Long, List<MessageVO>> map = list.stream().collect(Collectors.groupingBy(MessageVO::getThreadId));
		List<Long> notMatchedthreadIdList = new ArrayList<Long>();
		List<MessageVO> matchedMessgeList = new ArrayList<MessageVO>();
		List<MessageVO> notMatchedMessgeList = new ArrayList<MessageVO>();
		map.forEach((key, value) -> {
			if (value.size() >= limit) {
				matchedMessgeList.addAll(value.subList(0, limit));
			} else {
				notMatchedMessgeList.addAll(value);
				notMatchedthreadIdList.add(key);
			}
		});
		matchedMessgeList.stream().collect(Collectors.groupingBy(MessageVO::getThreadId)).forEach((key, value) -> {
			result.add(new MessageThreadVO(key, value.stream().sorted((a, b) -> a.getId().compareTo(b.getId())).collect(Collectors.toList())));
		});
		if (!CollectionUtils.isEmpty(notMatchedthreadIdList)) {
			// 判断是否要递归查询
			if (list.size() >= threadIdList.size() * limit) { // 数据量小于最小数据要求，则递归查
				result.addAll(this.getMessageThreadByIdList(notMatchedthreadIdList, limit)); // 递归查剩下的
			} else { // 不递归，就捞出没有加进去的数据
				notMatchedMessgeList.stream().collect(Collectors.groupingBy(MessageVO::getThreadId)).forEach((key, value) -> {
					result.add(new MessageThreadVO(key, value));
				});
			}
		}
		return result;
	}

	@Override
	public Page<MessageThreadVO> searchInteractionMessageThreads(Long memberId, Integer status, Integer pageNo, Integer pageSize) {
		Integer seeOthers = 1; // 是否看自己的
		if (memberId == null) {
			memberId = getCurrentUserId();
		}
		if (memberId.equals(getCurrentUserId())) {
			seeOthers = 0;
		}
		// 看自己的，查询所有相关数据就可以
		// 如果是看别人的，则 要求必须已经公开回复的 或者 回复给自己的

		Page<MessageThreadVO> page = new Page<>();
		page.setTotalCount(repository.searchInteractionMessageThreadsCount(memberId, getCurrentUserId(), status, seeOthers));
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		if (page.getTotalCount() > 0 && (page.getTotalCount() / pageSize + 1) >= pageNo) {
			List<MessageThreadVO> dataList = this
					.getMessageThread(repository.searchInteractionMessageThreads(memberId, getCurrentUserId(), status, seeOthers, (pageNo - 1) * pageSize, pageSize), 2);
			this.fillThreadInfo(dataList, 0);
			page.setData(dataList);
		}
		return page;
	}

	// viewType=1 我收到的 viewType=2 回复我的
	@Override
	public Page<MessageThreadVO> searchMyInteractionMessageThreads(Integer read, Integer viewType, Integer pageNo, Integer pageSize) {
		// 看自己的，查询所有相关数据就可以
		// 如果是看别人的，则 要求必须已经公开回复的 或者 回复给自己的
		Page<MessageThreadVO> page = new Page<>();
		Long memberId = SecurityContextHelper.getCurrentUserId();
		page.setTotalCount(repository.searchMyInteractionMessageThreadsCount(memberId, viewType));
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		if (page.getTotalCount() > 0 && (page.getTotalCount() / pageSize + 1) >= pageNo) {
			List<MessageThreadVO> dataList = this.getMessageThread(repository.searchMyInteractionMessageThreads(memberId, viewType, (pageNo - 1) * pageSize, pageSize), 2);
			this.fillThreadInfo(dataList, read);
			page.setData(dataList);
		}
		return page;
	}

	private void fillThreadInfo(List<MessageThreadVO> list, Integer read) {
		if (!CollectionUtils.isEmpty(list)) {
			List<Long> threadIdList = list.stream().map(item -> item.getId()).distinct().collect(Collectors.toList());
			List<MessageThreadMembersVO> tempList = this.messageThreadMembersService
					.list(QueryBuilder.create(MessageThreadMembersMapping.class).and(MessageThreadMembersMapping.THREADID, QueryOperator.IN, threadIdList).end());
			if (!CollectionUtils.isEmpty(tempList)) {
				List<Long> memberIdList = tempList.stream().map(item -> item.getMemberId()).distinct().collect(Collectors.toList());
				List<WholeMemberDTO> simpleMemberInfoList = memberService.getWholeMemberInfo(memberIdList);
				Map<Long, List<MessageThreadMembersVO>> map = tempList.stream().collect(Collectors.groupingBy(MessageThreadMembersVO::getThreadId));
				list.stream().forEach(item -> {
					List<MessageThreadMembersVO> memberList = map.get(item.getId());
					if (!CollectionUtils.isEmpty(memberList)) {
						memberList.stream().filter(m->m.getMemberId().equals(getCurrentUserId())).findAny().ifPresent(m->{
							item.setUnReadCount( m.getUnRead());
						});
						item.setMemberList(memberList.stream().map(m -> {
							Optional<WholeMemberDTO> opt = simpleMemberInfoList.stream().filter(a -> a.getId().equals(m.getMemberId())).findAny();
							return opt.isPresent() ? opt.get() : null;
						}).collect(Collectors.toList()));
						
					}
					// 匿名聊天不保留memberId
//					if (UCMessageThreadType.anonymousInteraction.getValue().equals(item.getType()) && !CollectionUtils.isEmpty(item.getMemberList())) {
//						if(!CollectionUtils.isEmpty(item.getLatestMessageList())) {
//							item.getLatestMessageList().stream().filter(m ->UCMessageType.interaction.getValue().equals(m.getType()) ).findAny().ifPresent(m ->{
//								m.setCreateBy(null);
//								m.setUpdateBy(null);
//								m.setMemberId(null);
//							});
//						}
//						item.setCreateBy(null);
//						item.setUpdateBy(null);
//						List<SimpleMemberInfo> mlist = new ArrayList<SimpleMemberInfo>();
//						// 隐藏头像，图片
//						item.getMemberList().stream().forEach(m -> {
//							// 干掉留言人的头像，id，昵称
//							if (!m.getId().equals(item.getOwnerId()) && !m.getId().equals(getCurrentUserId())) {
//								SimpleMemberInfo s = new SimpleMemberInfo();
//								if (m.getEducation() != null) {
//									EducationExperienceVO edu = new EducationExperienceVO();
//									BeanUtils.copyProperties(m.getEducation(), edu);
//									edu.setCreateBy(null);
//									edu.setUpdateBy(null);
//									s.setEducation(edu);
//								}
//								mlist.add(s);
//							} else {
//								mlist.add(m);
//							}
//						});
//						item.setMemberList(mlist);
//					}
				});
			}
		}
		if (read != null && read == 1) {
			List<MessageThreadMembersVO> readList = new ArrayList<MessageThreadMembersVO>();
			list.stream().forEach(item -> {
				Long lastMessageId = item.getLastMessageId();
				MessageThreadMembersVO mtmv = new MessageThreadMembersVO();
				mtmv.setLastPosition(lastMessageId);
				mtmv.setMemberId(SecurityContextHelper.getCurrentUserId());
				mtmv.setThreadId(item.getId());
				readList.add(mtmv);
			});
			this.messageThreadMembersService.batchRead(readList);
		}
	}

	@Override
	public MessageThreadVO getMessageThreadByMemberId(Long currentUserId, Integer read, Long memberId, Integer latestMessageCount) {
		// 检查是否好友
		if (!this.firendService.isFirends(currentUserId, memberId)) {
			throw new SystemException("只有相互认可后才能发送私信", CoreExceptionMessage.NO_PERMISSION);
		}

		MessageThreadVO thread = this.getOrCreateChatThread(Arrays.asList(currentUserId, memberId));
		thread = this.getBaseInfo(thread.getId());
		// 最多取10条
		if (latestMessageCount == null) {
			latestMessageCount = 10;
		}
		if (latestMessageCount > 10) {
			latestMessageCount = 10;
		}
		Page<MessageVO> page = this.messageService.page(QueryBuilder.create(MessageMapping.class).and(MessageMapping.THREADID, thread.getId()).end().sort(MessageMapping.CREATETIME)
				.desc().enablePaging().page(1, latestMessageCount).end());
		thread.setLatestMessageList(page.getData());
		return thread;
	}

	@Override
	public void onApplicationEvent(BusinessEvent<?> arg0) {
		if(arg0.isProcessed(getClass())) {
			return;
		}
		if (arg0 instanceof RecognizedCreateEvent) {
			arg0.logDetails();
			this.onRecognized(RecognizedCreateEvent.class.cast(arg0).getEventData());
		} else if (arg0 instanceof RecognizedDeleteEvent) {
			arg0.logDetails();
			this.onUnRecognized(RecognizedDeleteEvent.class.cast(arg0).getEventData());
		} else if (arg0 instanceof MessageSendEvent) {
			arg0.logDetails();
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
		mtmv.setId(messageThreadMembersService.list(QueryBuilder.create(MessageThreadMembersMapping.class).and(MessageThreadMembersMapping.THREADID, message.getThreadId())
				.and(MessageThreadMembersMapping.MEMBERID, getCurrentUserId()).end()).get(0).getId());
		mtmv.setLastPosition(messageId);
		mtmv.setUnRead(0);
		messageThreadMembersService.merge(mtmv);
	}

	@Override
	public Integer getLatestMessageCount(Long currentUserId) {
		return this.repository.getLatestMessageCount(currentUserId);
	}

	// viewType=1 我收到的 viewType=2 回复我的
	@Override
	public MessageThreadGroup searchMyInteractionMessageThreadsGroup(Integer viewType) {
		MessageThreadGroup group = new MessageThreadGroup();
		List<MessageThreadGroupPO> list = this.repository.searchMyInteractionMessageThreadsGroup(getCurrentUserId(), viewType);
		group.setTotalCount(0);
		if (!CollectionUtils.isEmpty(list)) {
			List<MessageThreadGroupItem> targetList = new ArrayList<MessageThreadGroupItem>(); // 要返回的数据
			List<MessageThreadGroupItem> publicTargetList = null;
			List<MessageThreadGroupItem> privateTargetList = new ArrayList<MessageThreadGroupItem>();
			// 我收到的才有可能匿名 viewType==1
			List<MessageThreadGroupPO> publicList = list.stream().filter(item -> (viewType == 1 && item.getType() == 2) || (viewType == 2)).collect(Collectors.toList());
			// 计算不匿名的数据
			if (!CollectionUtils.isEmpty(publicList)) {
				// viewType=1 查memberId，viewType=2 查ownerId，剔除掉匿名的
				List<Long> memberIdList = publicList.stream().map(item -> viewType == 1 ? item.getMemberId() : item.getOwnerId()).filter(item -> !item.equals(getCurrentUserId()))
						.distinct().collect(Collectors.toList());
				publicTargetList = memberIdList.stream().map(item -> {
					MessageThreadGroupItem a = new MessageThreadGroupItem();
					SimpleMemberInfo info = new SimpleMemberInfo();
					info.setId(item);
					a.setSimpleMemberInfo(info);
					return a;
				}).collect(Collectors.toList());
				Map<Long, List<MessageThreadGroupPO>> map = publicList.stream()
						.collect(Collectors.groupingBy(viewType == 1 ? MessageThreadGroupPO::getMemberId : MessageThreadGroupPO::getOwnerId));
				publicTargetList.forEach(item -> {// 计算总数，未读数
					List<MessageThreadGroupPO> joinedThreadList = map.get(item.getSimpleMemberInfo().getId()); // 参与的threadList
					if (!CollectionUtils.isEmpty(joinedThreadList)) {
						List<Long> joinedThreadIdList = joinedThreadList.stream().map(a -> a.getThreadId()).distinct().collect(Collectors.toList());
						// 总参与的thread id数量
						item.setTotalCount(joinedThreadIdList.size());
						// 因为是一来一回，所以未读数量= 总消息数量*2-thread数量
						List<MessageThreadGroupPO> all = publicList.stream().filter(t -> joinedThreadIdList.contains(t.getThreadId())).collect(Collectors.toList());
						item.setUnReadCount(item.getTotalCount() * 2 - all.size());
						joinedThreadIdList.sort((b, a) -> {
							return a.compareTo(b);
						});
						item.setLastetThreadId(joinedThreadIdList.get(0));
					}
				});
				targetList.addAll(publicTargetList);
			}
			// 计算匿名的数据
			List<MessageThreadGroupPO> privateList = list.stream().filter(item -> viewType == 1 && item.getType() == 3).collect(Collectors.toList());
			if (!CollectionUtils.isEmpty(privateList)) {
				// 匿名的
				Map<Long, List<MessageThreadGroupPO>> map = privateList.stream().collect(Collectors.groupingBy(MessageThreadGroupPO::getThreadId));
				map.forEach((key, value) -> {
					MessageThreadGroupItem item = new MessageThreadGroupItem();
					item.setLastetThreadId(key);
					item.setTotalCount(1); // 只会有一条
					item.setUnReadCount(2 - value.size()); // 总共2条
					privateTargetList.add(item);
				});
				targetList.addAll(privateTargetList);
			}
			// 总体排序
			targetList.sort((b, a) -> {
				return a.getLastetThreadId().compareTo(b.getLastetThreadId());
			});
			// 总数
			group.setTotalCount(targetList.stream().mapToInt(item -> item.getTotalCount()).sum());
			group.setUnReadTotalCount(targetList.stream().mapToInt(item -> item.getUnReadCount()).sum());
			group.setMemberTotalCount(targetList.size());
			// 返回前n条
			List<MessageThreadGroupItem> resultList = targetList.stream().limit(5).collect(Collectors.toList());
			// 填充头像信息
			List<Long> memberIdList = resultList.stream().filter(item -> item.getSimpleMemberInfo() != null).map(item -> item.getSimpleMemberInfo().getId())
					.collect(Collectors.toList());
			if (!CollectionUtils.isEmpty(memberIdList)) {
				List<SimpleMemberInfo> memberInfoList = this.memberService.getSimpleMemberInfo(memberIdList);
				resultList.stream().filter(item -> item.getSimpleMemberInfo() != null).forEach(item -> {
					memberInfoList.stream().filter(m -> m.getId().equals(item.getSimpleMemberInfo().getId())).findAny().ifPresent(m -> {
						item.setSimpleMemberInfo(m);
					});
				});
			}
			group.setList(resultList);
		}
		return group;
	}

}
