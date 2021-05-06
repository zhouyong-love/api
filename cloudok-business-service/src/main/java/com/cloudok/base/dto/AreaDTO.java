package com.cloudok.base.dto;

import com.cloudok.base.vo.SchoolVO;
import com.cloudok.core.po.PO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Author zhouyong
 * @Date 2021/5/4 0004 2:22
 * @Version 1.0
 */
@Setter
@Getter
public class AreaDTO extends PO {

    private static final long serialVersionUID = 1353591408048659100L;

    private Long id;

    private String name;

    private List<SchoolVO> schoolList;
}
