package com.cloudok.uc.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cloudok.base.service.TagService;
import com.cloudok.base.vo.TagVO;
import com.cloudok.core.context.SpringApplicationContext;
import com.cloudok.core.exception.CoreExceptionMessage;
import com.cloudok.core.exception.SystemException;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.service.AbstractService;
import com.cloudok.enums.TaggedType;
import com.cloudok.exception.CloudOKExceptionMessage;
import com.cloudok.security.SecurityContextHelper;
import com.cloudok.uc.event.MemberUpdateEvent;
import com.cloudok.uc.mapper.MemberTagsMapper;
import com.cloudok.uc.mapping.MemberTagsMapping;
import com.cloudok.uc.po.MemberTagsPO;
import com.cloudok.uc.service.MemberTagsService;
import com.cloudok.uc.vo.MemberTagsVO;
import com.cloudok.uc.vo.MemberVO;
import com.cloudok.uc.vo.SwitchSNRequest;

@Service
public class MemberTagsServiceImpl extends AbstractService<MemberTagsVO, MemberTagsPO> implements MemberTagsService{

	@Autowired
	private TagService tagService;
	
	@Autowired
	public MemberTagsServiceImpl(MemberTagsMapper repository) {
		super(repository);
	}


	@Override
	public MemberTagsVO create(MemberTagsVO d) {
		List<MemberTagsVO> list = super.list(QueryBuilder.create(MemberTagsMapping.class).and(MemberTagsMapping.TAGID, d.getTag().getId()).and(MemberTagsMapping.MEMBERID, getCurrentUserId()).end());
		if(!CollectionUtils.isEmpty(list)) {
			super.remove(list.stream().map(item->item.getId()).collect(Collectors.toList()));
		}
		d.setMemberId(SecurityContextHelper.getCurrentUserId());
		if(d.getTag().getId() == null) {
			d.getTag().setType(TaggedType.CUSTOM.getValue());
			d.setTag(this.tagService.create(d.getTag()));
		}
		d.setType(Integer.parseInt(TaggedType.CUSTOM.getValue()));
		if(d.getSn() == null || d.getSn() == 0) {
			List<MemberTagsVO> r = this.list(QueryBuilder.create(MemberTagsMapping.class)
					.and(MemberTagsMapping.MEMBERID, SecurityContextHelper.getCurrentUserId()).end());
			if(!CollectionUtils.isEmpty(r)) {
				d.setSn(r.stream().mapToInt(item -> item.getSn()).max().getAsInt()+1);
			}else {
				d.setSn(1);
			}
		}
		MemberTagsVO m = super.create(d);
		SpringApplicationContext.publishEvent(new MemberUpdateEvent(new MemberVO(SecurityContextHelper.getCurrentUserId())));
		return m;
	}

	@Override
	public MemberTagsVO update(MemberTagsVO d) {
		MemberTagsVO vo = this.get(d.getId());
		if (vo != null) {
			if (!vo.getMemberId().equals(SecurityContextHelper.getCurrentUserId())) {
				throw new SystemException(CoreExceptionMessage.NO_PERMISSION);
			}
		}
		if(d.getTag().getId() == null) {
			d.setType(Integer.parseInt(TaggedType.CUSTOM.getValue()));
			d.setTag(this.tagService.create(d.getTag()));;
		}
		d.setType(Integer.parseInt(TaggedType.CUSTOM.getValue()));
		d.setMemberId(SecurityContextHelper.getCurrentUserId());
		MemberTagsVO v = super.update(d);
		SpringApplicationContext.publishEvent(new MemberUpdateEvent(new MemberVO(SecurityContextHelper.getCurrentUserId())));
		return v;
	}

	@Override
	public Integer remove(Long pk) {
		MemberTagsVO vo = this.get(pk);
		if (vo != null) {
			if (!vo.getMemberId().equals(SecurityContextHelper.getCurrentUserId())) {
				throw new SystemException(CoreExceptionMessage.NO_PERMISSION);
			}
		}
		int r =  super.remove(pk);
		SpringApplicationContext.publishEvent(new MemberUpdateEvent(new MemberVO(SecurityContextHelper.getCurrentUserId())));
		return r;
	}


	@Override
	public MemberTagsPO convert2PO(MemberTagsVO d) {
		MemberTagsPO po = super.convert2PO(d);
		po.setTagId(d.getTag().getId());
		return po;
	}

	@Override
	public MemberTagsVO convert2VO(MemberTagsPO e) {
		MemberTagsVO vo = super.convert2VO(e);
		vo.setTag(new TagVO(e.getTagId()));
		return vo;
	}

	@Override
	public List<MemberTagsVO> convert2VO(List<MemberTagsPO> e) {
		List<MemberTagsVO> list = super.convert2VO(e);
		if (!CollectionUtils.isEmpty(list)) {
			List<TagVO> tagList = tagService.get(list.stream().map(item -> item.getTag().getId()).distinct().collect(Collectors.toList()));
			list.stream().forEach(item -> {
				tagList.stream().filter(i -> i.getId().equals(item.getTag().getId())).findAny().ifPresent(i -> {
					item.setTag(i);
				}); 
			});
		}

		return list;
	}

	@Override
	public List<MemberTagsVO> getByMember(Long currentUserId) {
		//只查用户给自己打的标签
		return this.list(QueryBuilder.create(MemberTagsMapping.class)
				.and(MemberTagsMapping.MEMBERID, SecurityContextHelper.getCurrentUserId()).and(MemberTagsMapping.TYPE, TaggedType.CUSTOM.getValue()).end());
	}


	@Override
	public MemberTagsVO getByMember(Long currentUserId, Long id) {
		return this.list(QueryBuilder.create(MemberTagsMapping.class)
				.and(MemberTagsMapping.MEMBERID, SecurityContextHelper.getCurrentUserId()).and(MemberTagsMapping.ID, id).end()).get(0);
	}
	
	@Override
	public Object switchSN(@Valid SwitchSNRequest switchSNRequest) {
		if(switchSNRequest.getSourceId() == null || switchSNRequest.getTargetId() == null) {
			throw new SystemException("数据记录不存在",CloudOKExceptionMessage.DEFAULT_ERROR);
		}
		List<MemberTagsVO> list = this.get(Arrays.asList(switchSNRequest.getSourceId(),switchSNRequest.getTargetId()));
		if(CollectionUtils.isEmpty(list) || list.size() != 2) {
			throw new SystemException("数据记录不存在",CloudOKExceptionMessage.DEFAULT_ERROR);
		}
		MemberTagsVO source = list.get(0);
		MemberTagsVO target = list.get(1);
		int sourceSn = source.getSn();
		int targetSn = target.getSn();
		source.setSn(targetSn);
		target.setSn(sourceSn);
		
		this.merge(source);
		this.merge(target);
		
		return true;
	}
}
