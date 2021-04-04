package com.cloudok.core.query;
import java.io.Serializable;

/**
 * 
 * @author zhijian.xia@foxmail.com
 * @date 2020年6月15日 下午9:45:49
 * @param <Q>
 */
public class PageCondition<Q extends QueryBuilder> implements Serializable {

	private static final long serialVersionUID = 7044193649614584740L;

	private Q q;
	
	private boolean enable=false;
	
	private int pageSize=20;
	
	private int pageNo=1;
	
	private static final int maxPageSzie = 200; //最大一次不超过200
	
	public boolean isEnable() {
		return enable;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public PageCondition<Q> enable(Q q) {
		this.enable=true;
		this.q=q;
		return this;
	}
	
	public PageCondition<Q> page(int pageNo,int pageSize) {
		if(pageSize<=0) {
			pageSize=10;
		}
		this.pageSize= Math.min(pageSize, maxPageSzie);
		if(pageNo<=0) {
			this.pageNo=1;
		}else {
			this.pageNo=pageNo;
		}
		return this;
	}
	
	/**
	 * 设置每页显示数量
	 * @param p
	 * @return
	 */
	public PageCondition<Q> pageSize(int p){
		if(p<=0) {
			p=10;
		}
		this.pageSize=Math.min(pageSize, maxPageSzie);
		return this;
	}
	
	/**
	 * 设置页码
	 * @param p
	 * @return
	 */
	public PageCondition<Q> pageNo(int p){
		if(p<=0) {
			this.pageNo=1;
		}else {
			this.pageNo=p;
		}
		return this;
	}
	/**
	 * 跳出分页设置
	 * @return
	 */
	public Q end() {
		return q;
	}
}
