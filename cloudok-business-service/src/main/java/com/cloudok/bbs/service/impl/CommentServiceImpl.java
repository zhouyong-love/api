package com.cloudok.bbs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudok.bbs.mapper.CommentMapper;
import com.cloudok.bbs.po.CommentPO;
import com.cloudok.bbs.service.CommentService;
import com.cloudok.bbs.vo.CommentVO;
import com.cloudok.core.exception.CoreExceptionMessage;
import com.cloudok.core.exception.SystemException;
import com.cloudok.core.service.AbstractService;
import com.cloudok.security.SecurityContextHelper;

@Service
public class CommentServiceImpl extends AbstractService<CommentVO, CommentPO> implements CommentService{

	@Autowired
	public CommentServiceImpl(CommentMapper repository) {
		super(repository);
	}
	

	@Override
	public CommentVO update(CommentVO d) {
		CommentVO vo = this.get(d.getId());
		if (vo != null) {
			if (!vo.getCreateBy().equals(SecurityContextHelper.getCurrentUserId())) {
				throw new SystemException(CoreExceptionMessage.NO_PERMISSION);
			}
		}
		return super.update(d);
	}
	
	@Override
	public Integer remove(Long pk) {
		CommentVO vo = this.get(pk);
		if (vo != null) {
			if (!vo.getCreateBy().equals(SecurityContextHelper.getCurrentUserId())) {
				throw new SystemException(CoreExceptionMessage.NO_PERMISSION);
			}
		}
		return super.remove(pk);
	}
}
