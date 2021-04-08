package com.cloudok.uc.task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

import com.cloudok.core.event.BusinessEvent;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.primarkey.SnowflakePrimaryKeyGenerator;
import com.cloudok.uc.dto.WholeMemberDTO;
import com.cloudok.uc.event.MemberScoreEvent;
import com.cloudok.uc.event.RecognizedCreateMemberScoreEvent;
import com.cloudok.uc.event.RecognizedDeletedMemberScoreEvent;
import com.cloudok.uc.mapper.MemberMapper;
import com.cloudok.uc.mapping.MemberMapping;
import com.cloudok.uc.mapping.RecognizedMapping;
import com.cloudok.uc.po.MemberPO;
import com.cloudok.uc.po.MemberSuggestScore;
import com.cloudok.uc.service.MemberService;
import com.cloudok.uc.service.RecognizedService;
import com.cloudok.uc.vo.EducationExperienceVO;
import com.cloudok.uc.vo.InternshipExperienceVO;
import com.cloudok.uc.vo.MemberTagsVO;
import com.cloudok.uc.vo.MemberVO;
import com.cloudok.uc.vo.RecognizedVO;
import com.cloudok.uc.vo.ResearchExperienceVO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MemberScoreCalcServiceV2 implements ApplicationListener<BusinessEvent<?>>,InitializingBean {

	private ExecutorService executor = Executors.newFixedThreadPool(10); // 最多同时8个线程并行

	@Autowired
	private MemberService memberService;
	
	@Autowired
	private MemberMapper repository;
	
	@Autowired
	private RecognizedService recognizedService;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		new Thread(()->{
			try {
				Thread.sleep(TimeUnit.MINUTES.toMillis(1));
			} catch (InterruptedException e) {
			}
//			 this.calcAll();
			this.fixedData();
		}) .start();
	}
	
	//修复下数据
	private void fixedData() {
		Long total = this.repository.getSuggestTotal();
		if(total == null || total.equals(0L)) {
			this.calcAll();
		}else {
			if(this.repository.getShouldFxixedRecognizedSize().compareTo(0L)>0 ) {
				 this.calcAll();		
			}
		}
	}
	public void initMemberScoreOnCreate(Long memberId) {
		int pageIndex = 1;
		List<MemberPO> memberList = repository.select(QueryBuilder.create(MemberMapping.class).sort(MemberMapping.ID).desc().enablePaging().page(pageIndex, 50).end());
		while(!CollectionUtils.isEmpty(memberList)) {
			List<MemberSuggestScore> list =	memberList.stream().filter(item -> !item.getId().equals(memberId))
			.map(item ->  {
				MemberSuggestScore s = new MemberSuggestScore(memberId,item.getId(),item.getWi(),0,0,0,0,0);
				s.setId(SnowflakePrimaryKeyGenerator.SEQUENCE.next());
				return s;
			})
				.collect(Collectors.toList());
			this.repository.createScoreList(list);
			pageIndex = pageIndex+1;
			memberList = repository.select(QueryBuilder.create(MemberMapping.class).sort(MemberMapping.ID).desc().enablePaging().page(pageIndex, 50).end());
		}
	}
	
	private void onMemberScoreEvent(MemberScoreEvent cast) {
		int pageNo = 1;
		QueryBuilder builder = QueryBuilder.create(MemberMapping.class).sort(MemberMapping.ID).desc().enablePaging().page(pageNo, 50).end();
		List<MemberVO> list = this.memberService.list(builder);
		MemberVO eventData = cast.getEventData();
		WholeMemberDTO owner = this.memberService.getWholeMemberInfo(eventData.getId());
		while(!CollectionUtils.isEmpty(list)) {
			List<WholeMemberDTO> targetList = this.memberService.getWholeMemberInfo(list.stream().filter(item -> !item.getId().equals(eventData.getId()))
					.map(item -> item.getId())
					.collect(Collectors.toList()));
			if(!CollectionUtils.isEmpty(targetList)) {
				//1 我与所有人的变更
				this.calcMemberScore(owner, targetList);
				//只要计算被改的人 与 其他人的评分
				targetList.stream().filter(item -> item.getId().equals(eventData.getId())).findAny().ifPresent(item ->{
					//2 所有人与我的变更
					this.calcMemberScore(item, Arrays.asList(owner));
				});
			}
			 pageNo = pageNo + 1;
			 builder = builder.enablePaging().page(pageNo, 50).end();
			 list = this.memberService.list(builder);
		}
		
	}

	//认可事件发生变更，只要计算两个人的值就可以
	private void onRecognizedDeletedMemberScoreEvent(RecognizedDeletedMemberScoreEvent cast) {
		//A->B 
		List<MemberSuggestScore> list = this.repository.getScoreByOwnerIdAndTargetId(cast.getEventData().getSourceId(),Arrays.asList(cast.getEventData().getTargetId()));
		list.stream().filter(item -> item.getTargetId().equals(cast.getEventData().getTargetId())).findAny().ifPresent(item ->{
			item.setRecognized(0);
			this.repository.updateScore(item);
		});
		//B->A  w(A,B)=w(A,B)-30
		List<MemberSuggestScore> list2 = this.repository.getScoreByOwnerIdAndTargetId(cast.getEventData().getTargetId(),Arrays.asList(cast.getEventData().getSourceId()));
		list2.stream().filter(item -> item.getTargetId().equals(cast.getEventData().getSourceId())).findAny().ifPresent(item ->{
			double s = item.getScore() == null ? 0 : item.getScore().doubleValue();
			item.setScore(s-30);
			this.repository.updateScore(item);
		});
	}
	//认可事件发生变更，只要计算两个人的值就可以
	private void onRecognizedCreateMemberScoreEvent(RecognizedCreateMemberScoreEvent cast) {
		//A->B
		List<MemberSuggestScore> list = this.repository.getScoreByOwnerIdAndTargetId(cast.getEventData().getSourceId(),Arrays.asList(cast.getEventData().getTargetId()));
		list.stream().filter(item -> item.getTargetId().equals(cast.getEventData().getTargetId())).findAny().ifPresent(item ->{
			item.setRecognized(1);
			this.repository.updateScore(item);
		});
		//B->A  w(A,B)=w(A,B)+30
		List<MemberSuggestScore> list2 = this.repository.getScoreByOwnerIdAndTargetId(cast.getEventData().getTargetId(),Arrays.asList(cast.getEventData().getSourceId()));
		list2.stream().filter(item -> item.getTargetId().equals(cast.getEventData().getSourceId())).findAny().ifPresent(item ->{
			double s = item.getScore() == null ? 0 : item.getScore().doubleValue();
			item.setScore(s+30);
			this.repository.updateScore(item);
		});
	}

	private List<WholeMemberDTO> getAllMemberList(){
		//空名片过滤掉
		List<MemberVO> memberList = memberService.list(QueryBuilder.create(MemberMapping.class).sort(MemberMapping.ID).desc());
		return memberService.getWholeMemberInfoByVOList(memberList);
	}
	
	private Integer getGrade(List<EducationExperienceVO> eduList) {
		int grade = 2100;
		for(EducationExperienceVO edu: eduList) {
			if("1".equals(edu.getDegree())) { //本科
				return edu.getGrade();
			}
			if("2".equals(edu.getDegree())) { //硕士
				grade = Math.min(grade, edu.getGrade());
			}
			if("3".equals(edu.getDegree())) {//博士
				grade = Math.min(grade, edu.getGrade());
			}
		}
		//最小的那个解4年
		return grade - 4;
	}
	//https://shimo.im/docs/kRYKX9PJJGycPWwT
	private void calcMemberScore(WholeMemberDTO owner,List<WholeMemberDTO> allList) {
		List<RecognizedVO> recognizedVOList = recognizedService.list(QueryBuilder.create(RecognizedMapping.class).and(RecognizedMapping.TARGETID, owner.getId()).end());
		List<RecognizedVO> myRecognizedVOList = recognizedService.list(QueryBuilder.create(RecognizedMapping.class).and(RecognizedMapping.SOURCEID, owner.getId()).end());
		List<WholeMemberDTO> otherList = allList.stream().filter(item -> !item.getId().equals(owner.getId())).collect(Collectors.toList());
		List<MemberSuggestScore> scoreList  = new ArrayList<MemberSuggestScore>();
		if(!CollectionUtils.isEmpty(otherList)) {
			otherList.stream().forEach(item ->{
			MemberSuggestScore score = new MemberSuggestScore(owner.getId(),item.getId(),0.0,0,0,0,0,0);
			scoreList.add(score);
			List<EducationExperienceVO> eduList = owner.getEducationList();
			List<EducationExperienceVO> othersEduList = item.getEducationList();
			if(!CollectionUtils.isEmpty(eduList) && !CollectionUtils.isEmpty(othersEduList) ) {
//				同校+20
//				for B的专业
//				     if(A也有这个专业) 大类相同+10 同细分专业+10
				eduList.stream().forEach(edu ->{
					othersEduList.stream().forEach(otherEdu ->{
						if(edu.getSchool().getId().equals(otherEdu.getSchool().getId())) {
							score.addScore(20.0);
							score.setSchool(1);
						}
						if(edu.getSpecialism().getId().equals(otherEdu.getSpecialism().getId())) {
							score.addScore(10.0);  //细分专业+10
							score.setSpecialism(2);
						}
						if(edu.getSpecialism().getCategory().equals(otherEdu.getSpecialism().getCategory())) {
							score.addScore(10.0);  //大类相同+10
							if(score.getSpecialism() != 2) {
								score.setSpecialism(1);
							}
						}
					});
				});
//				换算本科入学年级(硕博对应的年级减4)，年级相同+20，年级相隔一年+10，隔两年+0，隔三年-10，相隔大于三年减去20；
				int diff = Math.abs(getGrade(eduList)-getGrade(othersEduList));
				switch (diff) {
					case 0:
						score.addScore(20.0); 
						break;
					case 1:
						score.addScore(10.0); 
						break;
					case 2:
						score.addScore(0.0); 
						break;
					case 3:
						score.addScore(-10.0); 
						break;
					default:
						score.addScore(-20.0); 
						break;
				}
			}
			
//			研究方向
//			for B的研究方向
//			     if(A也有这个研究方向) +20
			List<ResearchExperienceVO> researchList = owner.getResearchList();
			List<ResearchExperienceVO> othersResearchList = item.getResearchList();
			if(!CollectionUtils.isEmpty(researchList) && !CollectionUtils.isEmpty(othersResearchList) ) {
				researchList.stream().forEach(research ->{
					othersResearchList.stream().forEach(otherResearch ->{
						if(research.getDomain().getId().equals(otherResearch.getDomain().getId())) {
							score.addScore(20.0);
						} 
					});
				});
			}
			
//			实习行业
//			for B的实习行业
//			     if(A也有这个实习行业) +20
//			for B的实习公司
//			     if(A也有这个公司) +20
			List<InternshipExperienceVO> internshipList = owner.getInternshipList();
			List<InternshipExperienceVO> othersInternshipList = item.getInternshipList();
			if(!CollectionUtils.isEmpty(internshipList) && !CollectionUtils.isEmpty(othersInternshipList) ) {
				internshipList.stream().forEach(internship ->{
					othersInternshipList.stream().forEach(otherInternship ->{
						if(internship.getIndustry().getId().equals(otherInternship.getIndustry().getId())) {
							score.addScore(10.0);
							if(score.getIndustry() != 2) {
								score.setIndustry(1);
							}
						} 
						if(internship.getIndustry().getCategory().equals(otherInternship.getIndustry().getCategory())) {
							score.addScore(10.0);
							score.setIndustry(2);
						}
						if(internship.getCompany().getId().equals(otherInternship.getCompany().getId())) {
							score.addScore(20.0);
						} 
					});
				});
			}
			
//			个性标签/状态标签
//			每有一个共同标签+10
			List<MemberTagsVO> tagList = owner.getTagsList();
			List<MemberTagsVO> othersTagList = item.getTagsList();
			if(!CollectionUtils.isEmpty(tagList) && !CollectionUtils.isEmpty(othersTagList) ) {
				tagList.stream().forEach(tag ->{
					othersTagList.stream().forEach(otherTag ->{
						if(tag.getTag().getId().equals(otherTag.getTag().getId())) {
							score.addScore(10.0);
							score.setTag(1);
						} 
					});
				});
			}
////			再加上B本身的profile分数Wi wi压制一万分，等于不推荐
			Double wi = item.getWi();
			if(wi == null || wi <= 0) {
				score.addScore(-10000.0);
			}
			
//			score.addScore(wi);
//			若B已经关注A，+30
			if(!CollectionUtils.isEmpty(recognizedVOList)) {
				recognizedVOList.stream().filter(r -> r.getSourceId().equals(item.getId())).findAny().ifPresent(r->{
					score.addScore(30.0);
				});
			}
			//A关注了B
			if(!CollectionUtils.isEmpty(myRecognizedVOList)) {
				myRecognizedVOList.stream().filter(r -> r.getTargetId().equals(item.getId())).findAny().ifPresent(r->{
					score.setRecognized(1);
				});
			}
//			推送机制：
//			按上述打分形成序列，已经推过的不再推(除非用户已经刷完了 再把之前推过的拉出来)，用户已经关注的都不推；
			});
		}
		if(!CollectionUtils.isEmpty(scoreList)) {
			List<MemberSuggestScore> dbList = this.repository.getScoreByOwnerId(Arrays.asList(owner.getId()));
			List<MemberSuggestScore>  updateList = new ArrayList<MemberSuggestScore>();
			List<MemberSuggestScore> createList = new ArrayList<MemberSuggestScore>();
			scoreList.stream().forEach(item ->{
				dbList.stream().filter(db -> db.getTargetId().equals(item.getTargetId()) ).findAny().ifPresent(db->{
					item.setId(db.getId());
					updateList.add(item);
				});
				if(item.getId() == null) {
					item.setId(SnowflakePrimaryKeyGenerator.SEQUENCE.next()); //设置主键id
					createList.add(item);
				}
			});
			if(!CollectionUtils.isEmpty(createList)) {
				this.repository.createScoreList(createList);
			}
			if(!CollectionUtils.isEmpty(updateList)) {
				updateList.stream().forEach(update -> {
					this.repository.updateScore(update);
				});
			}
			
			
		}
	}
	@Scheduled(cron="0 0 2 * * ? ")  //每天凌晨2点计算
	public void calcAll() {
		List<WholeMemberDTO> list = getAllMemberList();
		list.stream().forEach(item ->{
			this.calcMemberScore(item, list);
			Long count = this.repository.getUnSuggestCount(item.getId());
			if(count == null || count <  3) { 
				this.repository.resetSuggestStatus(item.getId());
			}
		});
	}

	@Override
	public void onApplicationEvent(BusinessEvent<?> arg0) {
		if(arg0.isProcessed(getClass())) {
			return;
		}
		if(
				arg0 instanceof MemberScoreEvent
				|| arg0 instanceof RecognizedCreateMemberScoreEvent
				|| arg0 instanceof RecognizedCreateMemberScoreEvent
				) {
			arg0.logDetails();
			executor.submit(() -> {
				Long start = System.currentTimeMillis();
					if (arg0 instanceof MemberScoreEvent) {
						this.onMemberScoreEvent(MemberScoreEvent.class.cast(arg0));
					} 
					if (arg0 instanceof RecognizedCreateMemberScoreEvent) {
						this.onRecognizedCreateMemberScoreEvent(RecognizedCreateMemberScoreEvent.class.cast(arg0));
					} 
					if (arg0 instanceof RecognizedDeletedMemberScoreEvent) {
						this.onRecognizedDeletedMemberScoreEvent(RecognizedDeletedMemberScoreEvent.class.cast(arg0));
					} 
					log.debug("用户推荐评分处理，事件为={}，耗时={} mils",arg0.getClass().getSimpleName(),(System.currentTimeMillis()-start));
			});
		}
	
	}


}
