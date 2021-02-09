package com.cloudok.log.vo;

public class InterceptorResult {
	private String formatedMessage;
	private Long businessId;
	private Long relationBusinessId;

	public String getFormatedMessage() {
		return formatedMessage;
	}

	public void setFormatedMessage(String formatedMessage) {
		this.formatedMessage = formatedMessage;
	}

	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}

	public Long getRelationBusinessId() {
		return relationBusinessId;
	}

	public void setRelationBusinessId(Long relationBusinessId) {
		this.relationBusinessId = relationBusinessId;
	}

}
