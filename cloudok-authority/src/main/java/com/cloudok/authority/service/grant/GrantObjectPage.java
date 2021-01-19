package com.cloudok.authority.service.grant;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GrantObjectPage implements Serializable {

	private static final long serialVersionUID = -2164558406776482638L;

	private int pageSize;

	private int pageNo;

	private Long totalCount;

	private List<Map<String, ?>> data;
}
