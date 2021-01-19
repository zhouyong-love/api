package com.cloudok.core.convert;

import java.util.List;

import com.cloudok.core.po.PO;
import com.cloudok.core.vo.VO;

/**
 * 
 * @author zhijian.xia@foxmail.com
 * @date 2020年6月14日 上午10:01:41
 * @param <PK>
 * @param <D>
 * @param <E>
 */

public interface Convert< D extends VO,E extends PO> {

	E convert2PO(D d);
	
	List<E> convert2PO(List<D> d);
	
	D convert2VO(E e);
	
	List<D> convert2VO(List<E> e);
}
