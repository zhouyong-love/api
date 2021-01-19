package com.cloudok.core.query;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.cloudok.core.exception.CoreExceptionMessage;
import com.cloudok.core.exception.SystemException;
import com.cloudok.core.mapping.Mapping;
import com.cloudok.core.po.PO;
import com.cloudok.core.vo.VO;
import com.cloudok.util.NumberUtil;
import com.cloudok.util.ReflectionUtils;

/**
 * 
 * @author zhijian.xia@foxmail.com
 * @date 2020年6月14日 上午10:02:33
 */

public class QueryBuilder {
	private Class<? extends Mapping> clazz;

	private List<QueryCondition<QueryBuilder>> conditions = new ArrayList<>();

	private List<SortCondition<QueryBuilder>> sortConditions = new ArrayList<>();

	private PageCondition<QueryBuilder> pageCondition;

	public List<QueryCondition<QueryBuilder>> getConditions() {
		return conditions;
	}

	public List<SortCondition<QueryBuilder>> getSortConditions() {
		return sortConditions;
	}

	public PageCondition<QueryBuilder> getPageCondition() {
		return pageCondition;
	}
	
	public <V extends VO> QueryBuilder with(V vo) {
		Field[] fs = ReflectionUtils.getAllFields(vo.getClass()); 
		Field[] configFields = ReflectionUtils.getAllFieldByType(clazz,Mapping.class);
		for (Field field : fs) {
			field.setAccessible(true);
			Object value=null;
			try {
				value = field.get(vo);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new SystemException(CoreExceptionMessage.FIELD_NOTFOUND_ERR);
			}
			if (!ObjectUtils.isEmpty(value)) {
				Mapping info = getMapping(configFields, field.getName());
				if (info == null) {
					throw new SystemException(clazz.getName()+" Mapping " + field.getName() + " Missing "+field.getName(),CoreExceptionMessage.FIELD_NOTFOUND_ERR);
				}
				and(info, value);
			}
		}
		return this;
	}
	
	public <P extends PO> QueryBuilder with(P p) {
		Field[] fs = ReflectionUtils.getAllFields(p.getClass()); 
		Field[] configFields = ReflectionUtils.getAllFieldByType(clazz,Mapping.class);
		for (Field field : fs) {
			field.setAccessible(true);
			Object value=null;
			try {
				value = field.get(p);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new SystemException(CoreExceptionMessage.FIELD_NOTFOUND_ERR);
			}
			if (!ObjectUtils.isEmpty(value)) {
				Mapping info = getMapping(configFields, field.getName());
				if (info == null) {
					throw new SystemException(clazz.getName()+" Mapping " + field.getName() + " Missing "+field.getName(),CoreExceptionMessage.FIELD_NOTFOUND_ERR);
				}
				and(info, value);
			}
		}
		return this;
	}
	
	/**
	 * 从request 构建查询
	 * 
	 * @param request
	 * @return
	 */
	public QueryBuilder with(HttpServletRequest request) {
		this.enablePaging().pageNo(1).pageSize(10);
		Enumeration<String> paramNames = request.getParameterNames();
		Field[] fieldMap = ReflectionUtils.getAllFields(clazz);
		while (paramNames.hasMoreElements()) {
			String paramName = paramNames.nextElement();
			switch (paramName) {
			case DynamicQueryParamName.PAGENO:
				this.enablePaging()
						.pageNo(NumberUtil.tryToInt(request.getParameter(DynamicQueryParamName.PAGENO), 1));
				break;
			case DynamicQueryParamName.PAGESIZE:
				this.enablePaging()
						.pageSize(NumberUtil.tryToInt(request.getParameter(DynamicQueryParamName.PAGESIZE), 10));
				break;
			case DynamicQueryParamName.SORT:
				pageSort(fieldMap, request.getParameter(DynamicQueryParamName.SORT));
				break;
			case DynamicQueryParamName.TEMPORARY_TOKEN:
				break;
			default:
				pageQueryParam(fieldMap,paramName, request.getParameter(paramName));
				break;
			}
		}
		return this;
	}
	
	/**
	 * 从map 构建
	 * @param request
	 * @return
	 */
	public QueryBuilder with(Map<String, ?> map) {
		Field[] fieldMap = ReflectionUtils.getAllFields(clazz);
		for (String paramName : map.keySet()) {
			switch (paramName) {
			case DynamicQueryParamName.PAGENO:
				this.enablePaging()
						.pageNo(NumberUtil.tryToInt(map.get(DynamicQueryParamName.PAGENO).toString(), 1));
				break;
			case DynamicQueryParamName.PAGESIZE:
				this.enablePaging()
						.pageSize(NumberUtil.tryToInt(map.get(DynamicQueryParamName.PAGESIZE).toString(), 10));
				break;
			case DynamicQueryParamName.SORT:
				pageSort(fieldMap, map.get(DynamicQueryParamName.SORT).toString());
				break;
			default:
				pageQueryParam(fieldMap,paramName, map.get(paramName));
				break;
			}
		}
		return this;
	}

	private void pageSort(Field[] fieldMap, String paramValue) {
		if (!StringUtils.isEmpty(paramValue)) {
			String[] sorts = paramValue.split(DynamicQueryParamName.SORT_SPLITE);
			for (String sort : sorts) {
				String sortName = sort;
				SortType sortType = SortType.ASC;
				if (sort.endsWith(DynamicQueryParamName.SORT_DESC)) {
					sortName = sortName.substring(0, sortName.length() - 1);
					sortType = SortType.DESC;
				} else if (sort.endsWith(DynamicQueryParamName.SORT_ASC)) {
					sortName = sortName.substring(0, sortName.length() - 1);
					sortType = SortType.ASC;
				} else {
					sortType = SortType.ASC;
				}
				
				Mapping fieldInfo = getMapping(fieldMap,sortName);
				if (fieldInfo == null || !fieldInfo.getSort()) {
					throw new SystemException(CoreExceptionMessage.SORT_ERR);
				}
				this.sortConditions
						.add(new SortCondition<>(this, fieldInfo,  sortType));
			}
		}
	}
	
	public static Mapping getMapping(Field[] fieldMap,String name) {
		for (Field field : fieldMap) {
			try {
				if(field.getType() == Mapping.class) {
					field.setAccessible(true);
					Mapping mapping = Mapping.class.cast(field.get(null));
					if(name.equalsIgnoreCase(mapping.getField())) {
						return mapping;
					}
				}
				
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new SystemException(CoreExceptionMessage.FIELD_NOTFOUND_ERR);
			}
		}
		return null;
	}

	private void pageQueryParam(Field[] fieldMap,String paramName_, Object paramValue) {
		if ((!StringUtils.isEmpty(paramName_))&&(!StringUtils.isEmpty(paramValue))) {
			String[] param = paramName_.split(DynamicQueryParamName.PARAM_SPLITE);
			String paramName = param[0];
			QueryOperator operator =QueryOperator.EQ;
			try {
				operator = param.length > 1 ? QueryOperator.valueOf(param[1].toUpperCase())
						: QueryOperator.EQ;
			} catch (Exception e) {
				throw new SystemException(CoreExceptionMessage.QUERYOPEAR_ERR);
			}
			Mapping fieldInfo = getMapping(fieldMap, paramName);
			if (fieldInfo == null || !hasOperator(fieldInfo.getOperator(), operator)) {
				throw new SystemException(clazz.getName()+" Mapping  Missing "+paramName,CoreExceptionMessage.FIELD_NOTFOUND_ERR);
			}
			Object value=paramValue;
			if(operator == QueryOperator.IN) {
				if(paramValue instanceof String) {
					value=paramValue.toString().split(DynamicQueryParamName.VALUE_SPLITE);
				}
			}
			this.conditions.add(new QueryCondition<QueryBuilder>(this, fieldInfo, value, operator, QuerySymbol.AND));
		}
	}
	
	private static boolean hasOperator(QueryOperator q[],QueryOperator q1) {
		if(ObjectUtils.isEmpty(q)) {
			return false;
		}
		for (QueryOperator QueryOperator : q) {
			if(QueryOperator.equals(q1)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 创建一个查询条件
	 * 
	 * @return
	 */
	public static<T extends Mapping> QueryBuilder create(Class<T> clazz) {
		QueryBuilder _queryStream = new QueryBuilder();
		_queryStream.clazz = clazz;
		return _queryStream;
	}

	/**
	 * 查询条件 or
	 * 
	 * @return
	 */
	public <V> QueryCondition<QueryBuilder> or(Mapping property, V value) {
		return or(property, QueryOperator.EQ, value);
	}
	
	/**
	 * 查询条件and
	 * 
	 * @return
	 */
	public <V> QueryCondition<QueryBuilder> and(Mapping property, V value) {
		return and(property, QueryOperator.EQ, value);
	}

	/**
	 * 查询条件 or
	 * 
	 * @return
	 */
	public <V> QueryCondition<QueryBuilder> or(Mapping property, QueryOperator queryOperator, V value) {
		QueryCondition<QueryBuilder> _condition = new QueryCondition<QueryBuilder>();
		_condition.init(this, property, value, queryOperator, QuerySymbol.OR);
		conditions.add(_condition);
		return _condition;
	}

	/**
	 * 查询条件and
	 * 
	 * @return
	 */
	public <V> QueryCondition<QueryBuilder> and(Mapping property, QueryOperator queryOperator, V value) {
		QueryCondition<QueryBuilder> _condition = new QueryCondition<QueryBuilder>();
		_condition.init(this, property, value, queryOperator, QuerySymbol.AND);
		conditions.add(_condition);
		return _condition;
	}

	/**
	 * 排序
	 * 
	 * @return
	 */
	public SortCondition<QueryBuilder> sort(Mapping prop) {
		Mapping voAnnotation =getMapping(ReflectionUtils.getAllFieldByType(clazz, Mapping.class), prop.getField());
		if (voAnnotation == null) {
			throw new SystemException(clazz.getName()+" Mapping Missing "+prop,CoreExceptionMessage.FIELD_NOTFOUND_ERR);
		}
		SortCondition<QueryBuilder> s = new SortCondition<>(this, prop);
		sortConditions.add(s);
		return s;
	}

	/**
	 * 启用分页
	 * 
	 * @return
	 */
	public PageCondition<QueryBuilder> enablePaging() {
		if (pageCondition == null) {
			pageCondition = new PageCondition<>();
		}
		pageCondition.enable(this);
		return pageCondition;
	}

	/**
	 * 禁用分页
	 * 
	 * @return
	 */
	public QueryBuilder disenablePaging() {
		pageCondition = null;
		return this;
	}
}
