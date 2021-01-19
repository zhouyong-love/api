package com.cloudok.core.mapper;

import java.util.List;

import com.cloudok.core.po.PO;
import com.cloudok.core.query.QueryBuilder;

/**
 * 
 * @author zhijian.xia@foxmail.com
 * @date 2020年6月14日 上午10:02:10
 * @param <PK>
 * @param <E>
 */

public abstract interface IMapper<E extends PO>{

	Integer create(E e);
	
	Integer update(E e);
	
	Integer merge(E e);
	
	Integer delete(List<Long> ids);
	
	Long count(QueryBuilder builder);
	
	List<E> select(QueryBuilder builder);
}
