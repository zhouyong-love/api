package com.cloudok.uc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudok.core.context.SpringApplicationContext;
import com.cloudok.core.exception.CoreExceptionMessage;
import com.cloudok.core.exception.SystemException;
import com.cloudok.core.service.AbstractService;
import com.cloudok.security.SecurityContextHelper;
import com.cloudok.uc.event.MemberUpdateEvent;
import com.cloudok.uc.event.RecognizedCreateEvent;
import com.cloudok.uc.event.RecognizedDeleteEvent;
import com.cloudok.uc.mapper.RecognizedMapper;
import com.cloudok.uc.po.RecognizedPO;
import com.cloudok.uc.service.RecognizedService;
import com.cloudok.uc.vo.MemberVO;
import com.cloudok.uc.vo.RecognizedVO;

@Service
public class RecognizedServiceImpl extends AbstractService<RecognizedVO, RecognizedPO> implements RecognizedService{

	private RecognizedMapper repository;
	
	@Autowired
	public RecognizedServiceImpl(RecognizedMapper repository) {
		super(repository);
		this.repository=repository;
	}
	
	@Override
	public RecognizedVO  create(RecognizedVO d) {
		d.setSourceId(SecurityContextHelper.getCurrentUserId());
		RecognizedVO v =  super.create(d);
		SpringApplicationContext.publishEvent(new RecognizedCreateEvent(v));
		SpringApplicationContext.publishEvent(new MemberUpdateEvent(new MemberVO(SecurityContextHelper.getCurrentUserId())));
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
		}
		int r = super.remove(id);
		SpringApplicationContext.publishEvent(new RecognizedDeleteEvent(vo));
		SpringApplicationContext.publishEvent(new MemberUpdateEvent(new MemberVO(SecurityContextHelper.getCurrentUserId())));
		return r;
	}
	@Override
	public int getFriendCount() {
		return repository.getFriendCount(getCurrentUserId());
	}

	@Override
	public int getNewApplyCount() {
		return repository.getNewApplyCount(getCurrentUserId());
	}
}
