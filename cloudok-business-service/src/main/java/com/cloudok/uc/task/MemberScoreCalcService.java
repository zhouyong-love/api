package com.cloudok.uc.task;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.cloudok.cache.Cache;
import com.cloudok.common.CacheType;
import com.cloudok.core.context.SpringApplicationContext;
import com.cloudok.core.event.BusinessEvent;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.query.QueryOperator;
import com.cloudok.enums.ActionType;
import com.cloudok.enums.MemberProfileType;
import com.cloudok.enums.UCMessageType;
import com.cloudok.log.event.UserActionEvent;
import com.cloudok.log.mapping.SysLogMapping;
import com.cloudok.log.service.SysLogService;
import com.cloudok.log.vo.SysLogVO;
import com.cloudok.uc.dto.WholeMemberDTO;
import com.cloudok.uc.event.MemberProfileEvent;
import com.cloudok.uc.event.MemberScoreEvent;
import com.cloudok.uc.event.MessageSendEvent;
import com.cloudok.uc.event.RecognizedCreateEvent;
import com.cloudok.uc.event.RecognizedCreateMemberScoreEvent;
import com.cloudok.uc.event.RecognizedDeleteEvent;
import com.cloudok.uc.event.RecognizedDeletedMemberScoreEvent;
import com.cloudok.uc.event.ViewMemberDetailEvent;
import com.cloudok.uc.mapping.MemberMapping;
import com.cloudok.uc.service.MemberService;
import com.cloudok.uc.vo.InternshipExperienceVO;
import com.cloudok.uc.vo.MemberTagsVO;
import com.cloudok.uc.vo.MemberVO;
import com.cloudok.uc.vo.MessageVO;
import com.cloudok.uc.vo.ProjectExperienceVO;
import com.cloudok.uc.vo.ResearchExperienceVO;
import com.cloudok.util.DateTimeUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MemberScoreCalcService implements ApplicationListener<BusinessEvent<?>>,InitializingBean {

	private ExecutorService executor = Executors.newFixedThreadPool(10); // 最多同时8个线程并行

	@Autowired
	private MemberService memberService;
	@Autowired
	private SysLogService sysLogService; 
	@Autowired
	private Cache cache;

	private void onMemberProfileEvent(MemberProfileEvent event) {
		if(event.getActionType() == ActionType.CREATE && event.getType() == MemberProfileType.base) {
			this.onMemberCreateEvent(event);
		}else {
			this.onMemberUpdateEvent(event);
		}
			
	}
	
	private void onMemberCreateEvent(MemberProfileEvent event) {
		WholeMemberDTO member = memberService.getWholeMemberInfo(event.getEventData());
		double score = calcMemberWIScore(member);
		MemberVO vo = new MemberVO();
		vo.setId(member.getId());
		vo.setWi(score);
		this.memberService.merge(vo);
		//触发评分计算
		SpringApplicationContext.publishEvent(new MemberScoreEvent(vo));
	}

	private double calcMemberWIScore(WholeMemberDTO member) {
		double score = 0;
		boolean onlyBaseInfo  = true;
		if(member.getAvatar() != null) {
			score = score + 10;
		}
		//基础信息：有头像+10分，有简介 + min(5, 简介字数/2)
		if(!StringUtils.isEmpty(member.getRemark()) ) {
			score = score + Math.min(5, member.getRemark().length()/2);
		}
		//多填一段教育经历+5分 (只填最初的一段不算分),封顶15分；
		if(!CollectionUtils.isEmpty(member.getEducationList()) && member.getEducationList().size()>1) {
			score = score + (member.getEducationList().size()-1)*5;
		}
		score = Math.min(score, 15);
//		填写了一段研究经历：有研究方向+2分，有项目课题+3分(项目课题可能为空)，有自定义描述 + min(5, 描述字数/3)
//		填写多段研究经历一样加分，封顶20分；
		if(!CollectionUtils.isEmpty(member.getResearchList())) {
			onlyBaseInfo = false;
			double temScore = 0;
			for(ResearchExperienceVO vo : member.getResearchList()) {
				temScore = temScore + (vo.getDomain()==null ? 0 : 2)+ (StringUtils.isEmpty(vo.getDescription()) ? 0 : Math.min(5, vo.getDescription().length()/3));
			}
			score = score + Math.min(temScore, 20);
		}
//		填写了一段实习经历：有公司+3分，有岗位+2分，有自定义描述 + min(5, 描述字数/3)；
//		填多段实习经历一样加分，封顶30分；
		
		if(!CollectionUtils.isEmpty(member.getInternshipList())) {
			onlyBaseInfo = false;
			double temScore = 0;
			for(InternshipExperienceVO vo : member.getInternshipList()) {
				temScore = temScore + (vo.getCompany()==null ? 0 : 3)+ (vo.getJob() ==null ? 0 : 2) + (StringUtils.isEmpty(vo.getDescription()) ? 0 : Math.min(5, vo.getDescription().length()/3));
			}
			score = score + Math.min(temScore, 30);
		}

//		填写了一段活动经历：有组织或角色+3分，有自定义描述 + min(3, 描述字数/3)；
//		填多段活动经历一样加分，封顶10分；
		if(!CollectionUtils.isEmpty(member.getProjectList())) {
			onlyBaseInfo = false;
			double temScore = 0;
			for(ProjectExperienceVO vo : member.getProjectList()) {
				temScore = temScore + ( !StringUtils.isEmpty(vo.getName()) || !StringUtils.isEmpty(vo.getJob()) ? 3 : 0)
						+ (StringUtils.isEmpty(vo.getDescription()) ? 0 : Math.min(3, vo.getDescription().length()/3));
			}
			score = score + Math.min(temScore, 10);
		}
//		个性标签&状态标签一起算：
//		选一个标签+2分，有自定义描述 + min(3, 描述字数/3)；
//		选多个标签一样加分，封顶25分；
		if(!CollectionUtils.isEmpty(member.getTagsList())) {
			onlyBaseInfo = false;
			double temScore = 0;
			for(MemberTagsVO vo : member.getTagsList()) {
				temScore = temScore  + 2   + (StringUtils.isEmpty(vo.getDescription()) ? 0 : Math.min(3, vo.getDescription().length()/3));
			}
			score = score + Math.min(temScore, 25);
		}
		score =  score > 100 ? 100 : score;
		if(onlyBaseInfo ) {
			return score-300; //空名片，设置为-300分
		}else {
			return score;
		}
	}

	private void onMemberUpdateEvent(MemberProfileEvent event) {
		WholeMemberDTO member = memberService.getWholeMemberInfo(event.getEventData());
		double score = calcMemberWIScore(member);
		MemberVO vo = new MemberVO();
		vo.setId(member.getId());
		vo.setWi(score);
		vo.setProfileUpdateTs(new Timestamp(System.currentTimeMillis()));
		this.memberService.merge(vo);
		//触发评分计算
		SpringApplicationContext.publishEvent(new MemberScoreEvent(vo));
	}

	private void onRecognizedCreateEvent(RecognizedCreateEvent event) {
		MemberVO member = memberService.get(event.getEventData().getSourceId());
		double score = member.getTi() == null ? 0 :  member.getTi().doubleValue();
		score = score + 3;
		MemberVO vo = new MemberVO();
		vo.setId(member.getId());
		vo.setTi(Math.min(score, 50));
		this.memberService.merge(vo);
		//触发评分计算
		SpringApplicationContext.publishEvent(new RecognizedCreateMemberScoreEvent(event.getEventData()));
	}
	
	private void onRecognizedDeleteEvent(RecognizedDeleteEvent event) {
		MemberVO member = memberService.get(event.getEventData().getSourceId());
		double score = member.getTi() == null ? 0 :  member.getTi().doubleValue();
		score = score - 3;
		MemberVO vo = new MemberVO();
		vo.setId(member.getId());
		vo.setTi(Math.max(Math.min(score, 50), 0));
		this.memberService.merge(vo);
		//触发评分计算
		SpringApplicationContext.publishEvent(new RecognizedDeletedMemberScoreEvent(event.getEventData()));
	}

	private void onViewMemberDetailEvent(ViewMemberDetailEvent event) {
		MemberVO member = memberService.get(event.getEventData().getLeft());
		double score = member.getTi() == null ? 0 :  member.getTi().doubleValue();
		score = score + 1;
		MemberVO vo = new MemberVO();
		vo.setId(member.getId());
		vo.setTi(Math.min(score, 50));
		this.memberService.merge(vo);
	}

	private void onMessageSendEvent(MessageSendEvent event) {
		MessageVO eventData = event.getEventData();
		MemberVO member = memberService.get(eventData.getMemberId());
		String type = eventData.getType();
		double score = member.getTi() == null ? 0 :  member.getTi().doubleValue();
		if(UCMessageType.chatMessage.getValue().endsWith(type)) {
			score = score + 2;
		}
		if(UCMessageType.interaction.getValue().endsWith(type)
				|| UCMessageType.privateInteractionReply.getValue().endsWith(type)
				|| UCMessageType.pubicInteractionReply.getValue().endsWith(type)
				) {
			score = score + 5;
		}
		MemberVO vo = new MemberVO();
		vo.setId(member.getId());
		vo.setTi(Math.min(score, 50));
		this.memberService.merge(vo);
	}

	//用户活动，每个时间段加10分
	private void onUserActionEvent(UserActionEvent event) {
		Long lastTime = cache.get(CacheType.Action, String.valueOf(event.getEventData().getUserId()), Long.class);
		if(lastTime !=  null) {
			return;
		}
		synchronized (this) {
			MemberVO member = memberService.get(event.getEventData().getUserId());
			if(member == null) {
				return;
			}
			lastTime = cache.get(CacheType.Action, String.valueOf(event.getEventData().getUserId()), Long.class);
			if(lastTime !=  null) {
				return;
			}
			if(lastTime == null) {
				double score = member.getTi() == null ? 0 :  member.getTi().doubleValue();
				MemberVO vo = new MemberVO();
				vo.setId(member.getId());
				vo.setTi(Math.min(score+10, 50));
				this.memberService.merge(vo);
				//每三个小时计算一次
				this.cache.put(CacheType.Action, String.valueOf(event.getEventData().getUserId()), System.currentTimeMillis(),3,TimeUnit.HOURS);
			}
		}
		
		
	}
//	每天从9:00-12:00, 12:00-15:00, 15:00-18:00, 18:00-21:00, 21:00-0:00，
//	这五个时间段，给一个时间段内没上线的打压 Ti*0.9，如在每天12:00时把上线时间早于9:00的用户Ti都*0.9；
	@Scheduled(cron="0 0 12,15,18,21 * * ?") 
	public void onLineScan() {
		int pageNo = 1;
		QueryBuilder builder = QueryBuilder.create(MemberMapping.class).sort(MemberMapping.ID).desc().enablePaging().page(pageNo, 100).end();
		Date end = new Date();
		Date start = DateTimeUtil.addDateByType(end, Calendar.HOUR_OF_DAY, -3);
		List<MemberVO> list = this.memberService.list(builder);
		while(!CollectionUtils.isEmpty(list)) {
			list.stream().forEach(item -> {
				//计算上一个时间段
				SysLogVO log = sysLogService.get(QueryBuilder.create(SysLogMapping.class)
						.and(SysLogMapping.USERID, item.getId())
						.and(SysLogMapping.CREATETIME, QueryOperator.GTE,start)
						.and(SysLogMapping.CREATETIME, QueryOperator.LTE,end)
						.end()
						.enablePaging().page(1, 1).end());
				if(log == null) {
					double score = item.getTi() == null ? 0 :  item.getTi().doubleValue();
					MemberVO vo = new MemberVO();
					vo.setId(item.getId());
					vo.setTi(score*0.9);
					this.memberService.merge(vo);
				}
			});
			 pageNo = pageNo + 1;
			 builder = builder.enablePaging().page(pageNo, 100).end();
			 list = this.memberService.list(builder);
		}
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		new Thread(()->{
			try {
				Thread.sleep(TimeUnit.MINUTES.toMillis(1));
			} catch (InterruptedException e) {
			}
			this.initScores();
//			this.initProfileUpdateTS();
		}) .start();
	}
	
	public void initScores() {
		int pageNo = 1;
		QueryBuilder builder = QueryBuilder.create(MemberMapping.class).and(MemberMapping.WI,0).end()
				.sort(MemberMapping.ID).desc().enablePaging().page(pageNo, 100).end();
		List<MemberVO> list = this.memberService.list(builder);
		while(!CollectionUtils.isEmpty(list)) {
			list.stream().forEach(item -> {
				SpringApplicationContext.publishEvent(MemberProfileEvent.update(item.getId(), MemberProfileType.base, item, item));
			});
			 pageNo = pageNo + 1;
			 builder = builder.enablePaging().page(pageNo, 100).end();
			 list = this.memberService.list(builder);
		}
	}
	
	public void initProfileUpdateTS() {
		int pageNo = 1;
		QueryBuilder builder = QueryBuilder.create(MemberMapping.class).and(MemberMapping.profileUpdateTs,QueryOperator.IE,null).end()
				.sort(MemberMapping.ID).desc();
		List<MemberVO> list = this.memberService.list(builder);
		if(!CollectionUtils.isEmpty(list)) {
			List<WholeMemberDTO> fullMemberList = this.memberService.getWholeMemberInfo(list.stream().map(item -> item.getId()).distinct().collect(Collectors.toList()));
			if(!CollectionUtils.isEmpty(fullMemberList)) {
				fullMemberList.stream().forEach(item ->{
					Timestamp lastTime = null;
//					if(item.getId().equals(4388329157914787840L)) {
//						System.out.println("debugg");
//					}
//					if(item.getId().equals(new Long(4388329157914787840L))) {
//						System.out.println("debugg");
//					}
					if(!CollectionUtils.isEmpty(item.getEducationList())) {
						Optional<Timestamp> opt = item.getEducationList().stream().map(r -> r.getUpdateTs()).max((a,b)-> a.compareTo(b));
						if(opt.isPresent()) {
							lastTime = opt.get();
						}
					}
					if(!CollectionUtils.isEmpty(item.getTagsList())) {
						Optional<Timestamp> opt = item.getTagsList().stream().map(r -> r.getUpdateTs()).max((a,b)-> a.compareTo(b));
						if(opt.isPresent()) {
							if(lastTime == null) {
								lastTime =  opt.get();;
							}else {
								lastTime = opt.get().compareTo(lastTime) > 0 ? opt.get() : lastTime;
							}
							
						}
					}
					if(!CollectionUtils.isEmpty(item.getResearchList())) {
						Optional<Timestamp> opt = item.getResearchList().stream().map(r -> r.getUpdateTs()).max((a,b)-> a.compareTo(b));
						if(opt.isPresent()) {
							if(lastTime == null) {
								lastTime =  opt.get();;
							}else {
								lastTime = opt.get().compareTo(lastTime) > 0 ? opt.get() : lastTime;
							}
						}
					}
					if(!CollectionUtils.isEmpty(item.getInternshipList())) {
						Optional<Timestamp> opt = item.getInternshipList().stream().map(r -> r.getUpdateTs()).max((a,b)-> a.compareTo(b));
						if(opt.isPresent()) {
							if(lastTime == null) {
								lastTime =  opt.get();;
							}else {
								lastTime = opt.get().compareTo(lastTime) > 0 ? opt.get() : lastTime;
							}
						}
					}
					if(!CollectionUtils.isEmpty(item.getProjectList())) {
						Optional<Timestamp> opt = item.getProjectList().stream().map(r -> r.getUpdateTs()).max((a,b)-> a.compareTo(b));
						if(opt.isPresent()) {
							if(lastTime == null) {
								lastTime =  opt.get();;
							}else {
								lastTime = opt.get().compareTo(lastTime) > 0 ? opt.get() : lastTime;
							}
						}
					} 
					if(lastTime == null) {
						SysLogVO log =	this.sysLogService.get(QueryBuilder.create(SysLogMapping.class)
								.and(SysLogMapping.USERID, item.getId())
								.and(SysLogMapping.REQUESTMETHOD,QueryOperator.NEQ,"GET")
								.end()
								.sort(SysLogMapping.ID).desc()
								);
						if(log != null) {
							lastTime = log.getCreateTs();
						}
					}
					if(lastTime == null) {
						lastTime = item.getCreateTs();
					}
					
					if(lastTime != null) {
						MemberVO m = new MemberVO();
						m.setId(item.getId());
						m.setProfileUpdateTs(lastTime);
						this.memberService.merge(m);
					}
				});
			}
			 pageNo = pageNo + 1;
		}
	}

	@Override
	public void onApplicationEvent(BusinessEvent<?> arg0) {
		if(arg0.isProcessed(getClass())) {
			return;
		}
		if (	   arg0 instanceof RecognizedCreateEvent 
				|| arg0 instanceof RecognizedDeleteEvent
				|| arg0 instanceof MemberProfileEvent
				|| arg0 instanceof MessageSendEvent
				|| arg0 instanceof ViewMemberDetailEvent
				|| arg0 instanceof UserActionEvent
				) {
			arg0.logDetails(); 

			executor.submit(() -> {
				Long start = System.currentTimeMillis();
				if (arg0 instanceof RecognizedCreateEvent) {
					this.onRecognizedCreateEvent(RecognizedCreateEvent.class.cast(arg0));
				}
				if (arg0 instanceof RecognizedDeleteEvent) {
					this.onRecognizedDeleteEvent(RecognizedDeleteEvent.class.cast(arg0));
				}
				if (arg0 instanceof MemberProfileEvent) {
					this.onMemberProfileEvent(MemberProfileEvent.class.cast(arg0));
				}

				if (arg0 instanceof MessageSendEvent) {
					this.onMessageSendEvent(MessageSendEvent.class.cast(arg0));
				}

				if (arg0 instanceof ViewMemberDetailEvent) {
					this.onViewMemberDetailEvent(ViewMemberDetailEvent.class.cast(arg0));
				}

				if (arg0 instanceof UserActionEvent) {
					this.onUserActionEvent(UserActionEvent.class.cast(arg0));
				}
				log.debug("用户评分处理,事件为:{}，耗时={} mils",arg0.getClass().getSimpleName(),(System.currentTimeMillis()-start));
				
			});
		}
		
		
	}



}
