package com.cloudok.core.vo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhijian.xia@foxmail.com
 * @date 2020年6月14日 上午10:29:54
 */
@Getter @Setter
public class Page<D extends VO> {

	private int pageSize;

	private int pageNo;

	private Long totalCount;

	private List<D> data;
}
