package com.cloudok.base.po;

import com.cloudok.core.po.PO;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author zhouyong
 * @Date 2021/5/4 0004 1:40
 * @Version 1.0
 */
@Getter
@Setter
public class AreaPO extends PO {

    private static final long serialVersionUID = 675695996244836500L;
    private String name;

    private Integer sn;
}
