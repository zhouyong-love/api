package com.cloudok.core.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.cloudok.core.context.SecurityContextAdapter;
import com.cloudok.core.context.SpringApplicationContext;
import com.cloudok.core.convert.Convert;
import com.cloudok.core.exception.CoreExceptionMessage;
import com.cloudok.core.exception.SystemException;
import com.cloudok.core.mapper.IMapper;
import com.cloudok.core.mapping.Mapping;
import com.cloudok.core.po.PO;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.query.QueryOperator;
import com.cloudok.core.vo.Page;
import com.cloudok.core.vo.VO;
import com.cloudok.primarkey.SnowflakePrimaryKeyGenerator;

/**
 * 
 * @author zhijian.xia@foxmail.com
 * @date 2020年6月14日 上午10:02:43
 * @param <PK>
 * @param <D>
 * @param <E>
 */

public abstract class AbstractService<D extends VO, E extends PO>
		implements IService< D, E>, Convert< D, E> {

	private IMapper<E> repository;

	private Convert<D, E> convert = this;

	private Class<D> voClass = null;

	private Class<E> poClass = null;

	public AbstractService(IMapper<E> repository) {
		this.repository = repository;
	}

	public AbstractService(IMapper<E> repository, Convert<D, E> convert) {
		this.repository = repository;
		this.convert = convert;
	}

	@Override
	public  E convert2PO(D d) {
		try {
			E e = getPOClass().getDeclaredConstructor().newInstance();
			BeanUtils.copyProperties(d, e);
			return e;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e1) {
			e1.printStackTrace();
			throw new SystemException(CoreExceptionMessage.CONVERT_ERROR, e1);
		}
	}

	@Override
	public  List<E> convert2PO(List<D> d) {
		List<E> es = new ArrayList<E>();
		if (d != null && d.size() > 0) {
			d.forEach(item -> es.add(convert2PO(item)));
		}
		return es;
	}

	@Override
	public  D convert2VO(E e) {
		try {
			D d = getVOClass().getDeclaredConstructor().newInstance();
			BeanUtils.copyProperties(e, d);
			return d;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e1) {
			e1.printStackTrace();
			throw new SystemException(CoreExceptionMessage.CONVERT_ERROR, e1);
		}
	}

	@Override
	public  List<D> convert2VO(List<E> e) {
		List<D> ds = new ArrayList<D>();
		if (e != null && e.size() > 0) {
			e.forEach(item -> ds.add(convert2VO(item)));
		}
		return ds;
	}

	@SuppressWarnings("unchecked")
	private Class<D> getVOClass() {
		if (voClass == null) {
			Type genType = this.getClass().getGenericSuperclass();
			if (!(genType instanceof ParameterizedType)) {
				return null;
			}
			voClass = (Class<D>) ((ParameterizedType) genType).getActualTypeArguments()[0];
		}
		return voClass;
	}

	@SuppressWarnings("unchecked")
	private Class<E> getPOClass() {
		if (poClass == null) {
			Type genType = this.getClass().getGenericSuperclass();
			if (!(genType instanceof ParameterizedType)) {
				return null;
			}
			poClass = (Class<E>) ((ParameterizedType) genType).getActualTypeArguments()[1];
		}
		return poClass;
	}

	@Override
	public D create(D d) {
		if (d.getId() == null) {
			d.setId(getPrimaryKey());
		}
		d.setDeleted(Boolean.FALSE);
		if(!getCurrentUserId().equals(0L) && d.getCreateBy() == null) {
			d.setCreateBy(getCurrentUserId());
		}
		d.setUpdateBy(d.getCreateBy());
		d.setCreateTs(new Timestamp(System.currentTimeMillis()));
		d.setUpdateTs(d.getCreateTs());
		d.setTenantId(getTenantId());
		repository.create(convert.convert2PO(d));
		return d;
	}

	@Transactional
	@Override
	public List<D> create(List<D> ds) {
		if(ds!=null) {
			ds.forEach(item->{
				create(item);
			});
		}
		return ds;
	}

	@Override
	public D merge(D d) {
		if(d.getId()==null) {
			throw new SystemException(CoreExceptionMessage.IDNULL_ERR);
		}
		d.setDeleted(Boolean.FALSE);
		if(!getCurrentUserId().equals(0L) && d.getUpdateBy() == null) {
			d.setUpdateBy(getCurrentUserId());
		}
		d.setUpdateTs(new Timestamp(System.currentTimeMillis()));
		repository.merge(convert.convert2PO(d));
		return d;
	}

	@Override
	public D update(D d) {
		if(d.getId()==null) {
			throw new SystemException(CoreExceptionMessage.IDNULL_ERR);
		}
		d.setDeleted(Boolean.FALSE);
		if(!getCurrentUserId().equals(0L) && d.getUpdateBy() == null) {
			d.setUpdateBy(getCurrentUserId());
		}
		d.setUpdateTs(new Timestamp(System.currentTimeMillis()));
		repository.update(convert.convert2PO(d));
		return d;
	}

	@Override
	public D get(Long id) {
		List<E> list = repository.select(QueryBuilder.create(Mapping.class).and(Mapping.ID, id).end());
		return list!=null&&list.size()>0?convert.convert2VO(list).get(0):null;
	}

	@Override
	public List<D> get(List<Long> pkList) {
		List<E> list = repository.select(QueryBuilder.create(Mapping.class).and(Mapping.ID,QueryOperator.IN, pkList).end());
		if(list!=null&&list.size()>0) {
			return convert.convert2VO(list);
		}
		return Collections.emptyList();
	}

	@Override
	public Long count(QueryBuilder queryStream) {
		return repository.count(queryStream);
	}

	@Override
	public List<D> list(QueryBuilder queryStream) {
		return convert.convert2VO(repository.select(queryStream));
	}

	@Override
	public Page<D> page(QueryBuilder queryStream) {
		Page<D> page=new Page<>();
		page.setTotalCount(count(queryStream.excludeSortPage()));
		page.setPageNo(queryStream.getPageCondition().getPageNo());
		page.setPageSize(queryStream.getPageCondition().getPageSize());
		if (page.getTotalCount() > 0 && (page.getTotalCount() / queryStream.getPageCondition().getPageSize() + 1) >= queryStream.getPageCondition()
				.getPageNo()) {
			page.setData(convert.convert2VO(repository.select(queryStream)));
		}
		return page;
	}

	@Override
	public Integer remove(Long pk) {
		return remove(Collections.singletonList(pk));
	}

	@Override
	public Integer remove(List<Long> pkList) {
		return repository.delete(pkList);
	}

	@Override
	public Long getPrimaryKey() {
		return SnowflakePrimaryKeyGenerator.SEQUENCE.next();
	}

	@Override
	public Long getCurrentUserId() {
		SecurityContextAdapter context=SpringApplicationContext.getBean(SecurityContextAdapter.class);
		return context!=null?context.getCurrentUserId():0L;
	}

	@Override
	public Long getTenantId() {
		SecurityContextAdapter context=SpringApplicationContext.getBean(SecurityContextAdapter.class);
		return context!=null?context.getTenantId():0L;
	}

	@Override
	public D get(QueryBuilder queryStream) {
		List<E> es = repository.select(queryStream.enablePaging().page(1, 1).end());
		if(CollectionUtils.isEmpty(es)) {
			return null;
		}
		if(es.size()>1) {
			throw new SystemException(CoreExceptionMessage.GET_MULTIPLE);
		}
		return convert2VO(es.get(0));
	}
}
