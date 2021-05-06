package com.cloudok.base.mapping;

import com.cloudok.core.mapping.Mapping;
import com.cloudok.core.query.QueryOperator;

/**
 * @Author zhouyong
 * @Date 2021/5/4 0004 1:30
 * @Version 1.0
 */
public class AreaMapping extends Mapping {

    private static final long serialVersionUID = 0L;

    public static final Mapping NAME = new Mapping("name", "t.name", QueryOperator.LIKE);

    public static final Mapping SN = new Mapping("sn", "t.sn",true);
}
