package com.cloudok.uc.po;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class MemberSuggestScore {
	private Long id;
	private Timestamp suggestTs;
	private Timestamp updateTs;
	private Long ownerId;
	private Long targetId;
	private Integer specialism;
	private Integer industry;
	private Integer tag;
	private Double score;
	
	public MemberSuggestScore() {
		
	}
	public MemberSuggestScore(Long ownerId,Long targetId,Double score,Integer specialism,Integer industry,Integer tag) {
		this.ownerId = ownerId;
		this.targetId  = targetId;
		this.score = score == null ? 0 : score;
		this.specialism = specialism;
		this.industry = industry;
		this.tag = tag;
	}
	public void addScore(Double score) {
		if(score == null) {
			return;
		}
		if(this.score == null) {
			this.score = 0.0;
		}
		this.score = this.score + score;
	}
}
