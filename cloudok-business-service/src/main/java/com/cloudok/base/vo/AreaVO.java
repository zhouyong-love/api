package com.cloudok.base.vo;

import com.cloudok.core.vo.VO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Author zhouyong
 * @Date 2021/5/4 0004 1:41
 * @Version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
public class AreaVO extends VO {

    private static final long serialVersionUID = 354021789966742100L;

    private String name;

    private Integer sn = 9999999;

    public AreaVO(Long id) {
        this.setId(id);
    }
}
