package com.cloudok.uc.vo;

import java.util.List;

import com.cloudok.uc.dto.SimpleMemberInfo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NotificationTotalVO {

	private Integer businessType;

	private Integer unReadCount;
	
	private Integer totalCount;

	private List<SimpleMemberInfo> latestMemberList;

}
