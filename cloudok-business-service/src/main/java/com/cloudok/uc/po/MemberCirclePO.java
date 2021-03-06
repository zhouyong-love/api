package com.cloudok.uc.po;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class MemberCirclePO {
	private Long memberId;
	private Timestamp lastUpdateTs;
}
