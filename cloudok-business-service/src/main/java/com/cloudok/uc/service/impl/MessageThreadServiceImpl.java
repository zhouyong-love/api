package com.cloudok.uc.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.cloudok.uc.service.*;
import com.cloudok.uc.vo.*;
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
	private NotificationService notificationService;

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
		// ????????????
		SpringApplicationContext.publishEvent(new MessageSendEvent(message));
	}

	private void onRecognized(RecognizedVO recognized) {
		// ??????????????????????????????????????????????????????????????????
		List<Long> memberIdList = Arrays.asList(recognized.getTargetId(), recognized.getSourceId());
		RecognizedVO sourceRecognized = this.recognizedService.get(QueryBuilder.create(RecognizedMapping.class).and(RecognizedMapping.SOURCEID, recognized.getTargetId())
				.and(RecognizedMapping.TARGETID, recognized.getSourceId()).end());
		if (sourceRecognized != null) { // ????????????????????? source ????????? target ??? target ????????? source
			MessageThreadVO thread = this.getOrCreateChatThread(memberIdList);
			this.sendMessage(thread.getId(), recognized.getSourceId(), UCMessageType.recognized.getValue(), "???????????????Peer??????????????????");
		}

	}

	private void onUnRecognized(RecognizedVO recognized) {
		List<Long> memberIdList = Arrays.asList(recognized.getTargetId(), recognized.getSourceId());
		MessageThreadPO thread = repository.getMessageThreadByMemebrs(memberIdList, UCMessageThreadType.chat.getValue());
		if (thread != null) { // ?????????????????????
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

	// type 1 ???????????? 2 ?????? 3 ?????? 4 ?????????????????? 5 ??????????????????
	@Override
	public MessageThreadVO createByMember(@Valid MessageVO vo) {
		if (vo.getToMemberId() == null) { // ?????????????????????????????????
			if (vo.getThreadId() == null || vo.getThreadId().equals(-1L)) {
				throw new SystemException("?????????????????????????????????????????????threadId", CoreExceptionMessage.PARAMETER_ERR);
			}
			List<MessageThreadMembersVO> list = this.messageThreadMembersService
					.list(QueryBuilder.create(MessageThreadMembersMapping.class).and(MessageThreadMembersMapping.THREADID, vo.getThreadId()).end());
			// ??????toMemberId
			list.stream().filter(item -> !item.getMemberId().equals(getCurrentUserId())).findAny().ifPresent(item -> {
				vo.setToMemberId(item.getMemberId());
			});
		}
		if (vo.getToMemberId().equals(getCurrentUserId())) {
			throw new SystemException("???????????????????????????", CoreExceptionMessage.PARAMETER_ERR);
		}
		if (StringUtils.isEmpty(vo.getType())) {
			throw new SystemException("????????????????????????", CoreExceptionMessage.PARAMETER_ERR);
		}
		if (UCMessageType.recognized.getValue().equals(vo.getType())) { // ??????
			throw new SystemException("?????????????????????????????????", CoreExceptionMessage.PARAMETER_ERR);
		}
		MessageThreadVO thread = null;
		// thread????????????????????????
		if (vo.getThreadId() == null || vo.getThreadId().equals(-1L)) {
			if (!UCMessageType.interaction.getValue().equals(vo.getType())) { // ?????????????????????threadId
				throw new SystemException("ThreadId????????????", CoreExceptionMessage.PARAMETER_ERR);
			}
			thread = this.createInteractionThread(vo.getToMemberId(), vo.isAnonymous(), Arrays.asList(vo.getMemberId(), vo.getToMemberId()));
			// ?????????????????????
			thread = this.getBaseInfo(thread.getId());
		} else {
			// ??????thread??????
			thread = this.getBaseInfo(vo.getThreadId());
		}
		// ?????????????????????
		if (!thread.getMemberList().stream().filter(item -> item.getId() != null).map(item -> item.getId()).filter(item -> item.equals(SecurityContextHelper.getCurrentUserId())).findAny().isPresent()) {
			throw new SystemException("?????????????????????", CoreExceptionMessage.NO_PERMISSION);
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
			throw new SystemException("?????????????????????", CoreExceptionMessage.NO_PERMISSION);
		}
		// ?????????????????????
		if (thread.getIsPublic() == false
				&& !thread.getMemberList().stream().map(item -> item.getId()).filter(item -> item.equals(SecurityContextHelper.getCurrentUserId())).findAny().isPresent()) {
			throw new SystemException("?????????????????????", CoreExceptionMessage.NO_PERMISSION);
		}
		// ????????????
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
		// ????????????????????????????????????????????????
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
		// ?????? threadIdList.size * limit * 5 ??? ?????????????????????thread????????????limit???????????????????????????
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
			// ???????????????????????????
			if (list.size() >= threadIdList.size() * limit) { // ????????????????????????????????????????????????
				result.addAll(this.getMessageThreadByIdList(notMatchedthreadIdList, limit)); // ??????????????????
			} else { // ?????????????????????????????????????????????
				notMatchedMessgeList.stream().collect(Collectors.groupingBy(MessageVO::getThreadId)).forEach((key, value) -> {
					result.add(new MessageThreadVO(key, value));
				});
			}
		}
		return result;
	}

	@Override
	public Page<MessageThreadVO> searchInteractionMessageThreads(Long memberId, Integer status, Integer pageNo, Integer pageSize) {
		Integer seeOthers = 1; // ??????????????????
		if (memberId == null) {
			memberId = getCurrentUserId();
		}
		if (memberId.equals(getCurrentUserId())) {
			seeOthers = 0;
		}
		// ????????????????????????????????????????????????
		// ??????????????????????????? ????????????????????????????????? ?????? ??????????????????

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

	// viewType=1 ???????????? viewType=2 ????????????
	@Override
	public Page<MessageThreadVO> searchMyInteractionMessageThreads(Integer read, Integer viewType, Integer pageNo, Integer pageSize) {
		// ????????????????????????????????????????????????
		// ??????????????????????????? ????????????????????????????????? ?????? ??????????????????
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
					// ?????????????????????memberId
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
//						// ?????????????????????
//						item.getMemberList().stream().forEach(m -> {
//							// ???????????????????????????id?????????
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
				mtmv.setUnRead(1);
				readList.add(mtmv);
			});
			this.messageThreadMembersService.batchRead(readList);
		}
	}

	@Override
	public MessageThreadVO getMessageThreadByMemberId(Long currentUserId, Integer read, Long memberId, Integer latestMessageCount) {
		// ??????????????????
		if (!this.firendService.isFirends(currentUserId, memberId)) {
			throw new SystemException("???????????????????????????????????????", CoreExceptionMessage.NO_PERMISSION);
		}

		MessageThreadVO thread = this.getOrCreateChatThread(Arrays.asList(currentUserId, memberId));
		thread = this.getBaseInfo(thread.getId());
		// ?????????10???
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
		Integer notificationUnReadCount = notificationService.getUnReadCount();
		return this.repository.getLatestMessageCount(currentUserId) + notificationUnReadCount;
	}

	// viewType=1 ???????????? viewType=2 ????????????
	@Override
	public MessageThreadGroup searchMyInteractionMessageThreadsGroup(Integer viewType) {
		MessageThreadGroup group = new MessageThreadGroup();
		List<MessageThreadGroupPO> list = this.repository.searchMyInteractionMessageThreadsGroup(getCurrentUserId(), viewType);
		group.setTotalCount(0);
		if (!CollectionUtils.isEmpty(list)) {
			List<MessageThreadGroupItem> targetList = new ArrayList<MessageThreadGroupItem>(); // ??????????????????
			List<MessageThreadGroupItem> publicTargetList = null;
			List<MessageThreadGroupItem> privateTargetList = new ArrayList<MessageThreadGroupItem>();
			// ?????????????????????????????? viewType==1
			List<MessageThreadGroupPO> publicList = list.stream().filter(item -> (viewType == 1 && item.getType() == 2) || (viewType == 2)).collect(Collectors.toList());
			// ????????????????????????
			if (!CollectionUtils.isEmpty(publicList)) {
				// viewType=1 ???memberId???viewType=2 ???ownerId?????????????????????
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
				publicTargetList.forEach(item -> {// ????????????????????????
					List<MessageThreadGroupPO> joinedThreadList = map.get(item.getSimpleMemberInfo().getId()); // ?????????threadList
					if (!CollectionUtils.isEmpty(joinedThreadList)) {
						List<Long> joinedThreadIdList = joinedThreadList.stream().map(a -> a.getThreadId()).distinct().collect(Collectors.toList());
						// ????????????thread id??????
						item.setTotalCount(joinedThreadIdList.size());
						// ??????????????????????????????????????????= ???????????????*2-thread??????
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
			// ?????????????????????
			List<MessageThreadGroupPO> privateList = list.stream().filter(item -> viewType == 1 && item.getType() == 3).collect(Collectors.toList());
			if (!CollectionUtils.isEmpty(privateList)) {
				// ?????????
				Map<Long, List<MessageThreadGroupPO>> map = privateList.stream().collect(Collectors.groupingBy(MessageThreadGroupPO::getThreadId));
				map.forEach((key, value) -> {
					MessageThreadGroupItem item = new MessageThreadGroupItem();
					item.setLastetThreadId(key);
					item.setTotalCount(1); // ???????????????
					item.setUnReadCount(2 - value.size()); // ??????2???
					privateTargetList.add(item);
				});
				targetList.addAll(privateTargetList);
			}
			// ????????????
			targetList.sort((b, a) -> {
				return a.getLastetThreadId().compareTo(b.getLastetThreadId());
			});
			// ??????
			group.setTotalCount(targetList.stream().mapToInt(item -> item.getTotalCount()).sum());
			group.setUnReadTotalCount(targetList.stream().mapToInt(item -> item.getUnReadCount()).sum());
			group.setMemberTotalCount(targetList.size());
			// ?????????n???
			List<MessageThreadGroupItem> resultList = targetList.stream().limit(5).collect(Collectors.toList());
			// ??????????????????
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
