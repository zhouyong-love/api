package com.cloudok.core.service;

import java.util.List;

import com.cloudok.core.po.PO;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.vo.Page;
import com.cloudok.core.vo.VO;

/**
 * 
 * @author zhijian.xia@foxmail.com
 * @date 2020年6月14日 上午10:02:49
 * @param <PK>
 * @param <D>
 * @param <E>
 */

public interface IService<D extends VO, E extends PO> {

	D create(D d);

	List<D> create(List<D> ds);
	
	D merge(D d);
	
	D update(D d);
	
	D get(Long id);
	
	List<D> get(List<Long> pkList);
	
	Long count(QueryBuilder queryStream);
	
	List<D> list(QueryBuilder queryStream);
	
	Page<D> page(QueryBuilder queryStream);
	
	Integer remove(Long pk);
	
	Integer remove(List<Long> pkList);
	
	Long getPrimaryKey();
	
	Long getCurrentUserId();
	
	Long getTenantId();
}
