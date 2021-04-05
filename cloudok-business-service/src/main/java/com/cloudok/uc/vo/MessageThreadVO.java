package com.cloudok.uc.vo;

import java.sql.Timestamp;
import java.util.List;

import com.cloudok.core.vo.VO;
import com.cloudok.uc.dto.WholeMemberDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageThreadVO extends VO {

	private static final long serialVersionUID = 859537390951971200L;

	private String type;

	private Long ownerId;

	private Long lastMessageId;
	
	private Boolean isPublic;

	private Integer unReadCount;
	
	private Timestamp lastUpdate;
	
	/**
	 * 参与者
	 */
	private List<WholeMemberDTO> memberList;
	
	/**
	 * 最新消息
	 */
	private List<MessageVO> latestMessageList;
	
	public MessageThreadVO(Long id,List<MessageVO> latestMessageList) {
		this.setId(id);
		this.setLatestMessageList(latestMessageList);
	}
}
