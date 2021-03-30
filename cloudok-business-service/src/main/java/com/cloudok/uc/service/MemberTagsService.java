package com.cloudok.uc.service;

import java.util.List;

import javax.validation.Valid;

import com.cloudok.core.event.BusinessEvent;
import com.cloudok.core.service.IService;
import com.cloudok.uc.po.MemberTagsPO;
import com.cloudok.uc.vo.MemberTagsVO;
import com.cloudok.uc.vo.SwitchSNRequest;

public interface MemberTagsService extends IService<MemberTagsVO, MemberTagsPO> {

	List<MemberTagsVO> getByMember(Long currentUserId);

	MemberTagsVO getByMember(Long currentUserId, Long id);

	Object switchSN(@Valid SwitchSNRequest switchSNRequest);

	MemberTagsVO createByMember(MemberTagsVO d);

	MemberTagsVO updateByMember(MemberTagsVO d);
	
	/**
	 * 第一次数据同步使用，后面不会用了
	 * @param memberId
	 * @param topicId
	 */
	@Deprecated
	void sysnc(Long memberId,Long topicId);

	/**
	 * 强制先调用这个 再发布post事件，防止topic计数出现问题
	 * @param event
	 */
	public void onPostChange(BusinessEvent<?> event);
}
