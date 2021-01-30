package com.cloudok.bbs.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cloudok.bbs.mapper.CommentMapper;
import com.cloudok.bbs.po.CommentPO;
import com.cloudok.bbs.service.CommentService;
import com.cloudok.bbs.vo.CommentVO;
import com.cloudok.core.exception.CoreExceptionMessage;
import com.cloudok.core.exception.SystemException;
import com.cloudok.core.service.AbstractService;
import com.cloudok.security.SecurityContextHelper;
import com.cloudok.uc.dto.SimpleMemberInfo;
import com.cloudok.uc.dto.WholeMemberDTO;
import com.cloudok.uc.service.MemberService;

@Service
public class CommentServiceImpl extends AbstractService<CommentVO, CommentPO> implements CommentService{

	@Autowired
	private MemberService memberService;
	
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
	
	@Override
	public List<CommentVO> convert2VO(List<CommentPO> e) {
		List<CommentVO> list = super.convert2VO(e);
		if (!CollectionUtils.isEmpty(list)) {
			List<WholeMemberDTO> members = memberService.getWholeMemberInfo(list.stream().map(item -> item.getCreateBy()).distinct().collect(Collectors.toList()));
			List<SimpleMemberInfo> simpleList =  members.stream().map(item -> item.toSampleInfo()).collect(Collectors.toList());
			list.stream().forEach(item -> {
				simpleList.stream().filter(mem -> mem.getId().equals(item.getCreateBy())).findAny().ifPresent(mem -> {
					item.setMember(mem);
				});
			});
		}
		return list;
	}

}
