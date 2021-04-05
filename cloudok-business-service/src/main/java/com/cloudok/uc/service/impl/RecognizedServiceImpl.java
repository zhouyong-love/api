package com.cloudok.uc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cloudok.core.context.SpringApplicationContext;
import com.cloudok.core.exception.CoreExceptionMessage;
import com.cloudok.core.exception.SystemException;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.query.QueryOperator;
import com.cloudok.core.service.AbstractService;
import com.cloudok.core.vo.Page;
import com.cloudok.exception.CloudOKExceptionMessage;
import com.cloudok.security.SecurityContextHelper;
import com.cloudok.uc.event.RecognizedCreateEvent;
import com.cloudok.uc.event.RecognizedDeleteEvent;
import com.cloudok.uc.mapper.RecognizedMapper;
import com.cloudok.uc.mapping.FirendMapping;
import com.cloudok.uc.mapping.RecognizedMapping;
import com.cloudok.uc.po.RecognizedPO;
import com.cloudok.uc.service.FirendService;
import com.cloudok.uc.service.MemberService;
import com.cloudok.uc.service.RecognizedService;
import com.cloudok.uc.vo.RecognizedVO;

@Service
public class RecognizedServiceImpl extends AbstractService<RecognizedVO, RecognizedPO> implements RecognizedService{

	private RecognizedMapper repository;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private RecognizedService recognizedService;
	
	@Autowired
	private FirendService firendService;
	
	@Autowired
	public RecognizedServiceImpl(RecognizedMapper repository) {
		super(repository);
		this.repository=repository;
	}
	
	@Override
	public RecognizedTotalDTO recognized(RecognizedVO vo) {
		if(vo.getTargetId().equals(getCurrentUserId())) {
			throw new SystemException(CloudOKExceptionMessage.RECOGNIZED_SELF);
		}
		this.create(vo);
		return RecognizedTotalDTO.builder().friendCount(firendService.count(QueryBuilder.create(FirendMapping.class).and(FirendMapping.SOURCEID, getCurrentUserId()).end()))
				.fromCount(recognizedService.count(QueryBuilder.create(RecognizedMapping.class).and(RecognizedMapping.TARGETID, getCurrentUserId()).end()))
				.toCount(recognizedService.count(QueryBuilder.create(RecognizedMapping.class).and(RecognizedMapping.SOURCEID, getCurrentUserId()).end())).build();
	}
	
	@Override
	public RecognizedVO  create(RecognizedVO d) {
		if(!memberService.checkMemberNotEmpty(d.getSourceId())) {
			throw new SystemException(CloudOKExceptionMessage.INCOMPLETE_USER_INFORMATION);
		}
		if(count(QueryBuilder.create(RecognizedMapping.class).and(RecognizedMapping.SOURCEID, SecurityContextHelper.getCurrentUserId()).and(RecognizedMapping.TARGETID, d.getTargetId()).end())>0) {
			throw new SystemException("请勿重复可");
		}
		d.setSourceId(SecurityContextHelper.getCurrentUserId());
		d.setRead(false);
		RecognizedVO v =  super.create(d);
		SpringApplicationContext.publishEvent(new RecognizedCreateEvent(v));
//		SpringApplicationContext.publishEvent(new MemberUpdateEvent(new MemberVO(SecurityContextHelper.getCurrentUserId())));
		return v;
	}
//	@Override
//	public RecognizedVO update(RecognizedVO d) { 
//		d.setSourceId(SecurityContextHelper.getCurrentUserId());
//		RecognizedVO vo = this.get(d.getId());
//		if (vo != null) {
//			if (!vo.getSourceId().equals(SecurityContextHelper.getCurrentUserId())) {
//				throw new SystemException(CoreExceptionMessage.NO_PERMISSION);
//			}
//		}
//		RecognizedVO v = super.update(d);
//		SpringApplicationContext.publishEvent(new MemberUpdateEvent(new MemberVO(SecurityContextHelper.getCurrentUserId())));
//		return v;
//	}
	@Override
	public Integer remove(Long id) {
		RecognizedVO vo = this.get(id);
		if (vo != null) {
			if (!vo.getSourceId().equals(SecurityContextHelper.getCurrentUserId())) {
				throw new SystemException(CoreExceptionMessage.NO_PERMISSION);
			}
		}else {
			return 0;
		}
		if(count(QueryBuilder.create(RecognizedMapping.class).and(RecognizedMapping.TARGETID, vo.getSourceId()).and(RecognizedMapping.SOURCEID, vo.getTargetId()).end())>0) {
			throw new SystemException("已互可，不能取消");
		}
		int r = super.remove(id);
		SpringApplicationContext.publishEvent(new RecognizedDeleteEvent(vo));
//		SpringApplicationContext.publishEvent(new MemberUpdateEvent(new MemberVO(SecurityContextHelper.getCurrentUserId())));
		return r;
	}
	@Deprecated
	@Override
	public int getFriendCount() {
		return repository.getFriendCount(getCurrentUserId());
	}

	@Override
	public int getNewApplyCount() {
		return repository.getNewApplyCount(getCurrentUserId());
	}

	@Override
	public void read(List<Long> memberIds) {
		if(!CollectionUtils.isEmpty(memberIds)) {
			List<RecognizedVO> vos = super.list(QueryBuilder.create(RecognizedMapping.class).and(RecognizedMapping.SOURCEID, QueryOperator.IN,memberIds).and(RecognizedMapping.TARGETID, getCurrentUserId()).end());
			if(!CollectionUtils.isEmpty(vos)) {
				vos.forEach(item->{
					super.merge(new RecognizedVO(item.getId(),true));
				});
			}
		}
	}

	@Override
	public RecognizedTotalDTO unRecognized(Long memberId) {
		List<RecognizedVO> vos = super.list(QueryBuilder.create(RecognizedMapping.class).and(RecognizedMapping.SOURCEID, getCurrentUserId()).and(RecognizedMapping.TARGETID, memberId).end());
		if(!CollectionUtils.isEmpty(vos)) {
			vos.forEach(vo->{
				remove(vo.getId());
			});
		}
		return RecognizedTotalDTO.builder().friendCount(firendService.count(QueryBuilder.create(FirendMapping.class).and(FirendMapping.SOURCEID, getCurrentUserId()).end()))
				.fromCount(recognizedService.count(QueryBuilder.create(RecognizedMapping.class).and(RecognizedMapping.TARGETID, getCurrentUserId()).end()))
				.toCount(recognizedService.count(QueryBuilder.create(RecognizedMapping.class).and(RecognizedMapping.SOURCEID, getCurrentUserId()).end())).build();
	}

	@Override
	public Page<RecognizedVO> getSecondDegreeRecognized(Long currentUserId, Long memberId, Integer pageNo,
			Integer pageSize) {
			Page<RecognizedVO> page=new Page<>();
			page.setTotalCount(repository.getSecondDegreeRecognizedCount(currentUserId,memberId).longValue());
			page.setPageNo(pageNo);
			page.setPageSize(pageSize);
			if (page.getTotalCount() > 0 && (page.getTotalCount() / pageSize + 1) >= pageNo) {
				page.setData(this.convert2VO(repository.getSecondDegreeRecognized(currentUserId,memberId,(pageNo-1)*pageSize,pageSize)));
			}
			return page;
	}
}
