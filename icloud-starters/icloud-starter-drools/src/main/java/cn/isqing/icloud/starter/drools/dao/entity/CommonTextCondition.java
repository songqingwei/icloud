package cn.isqing.icloud.starter.drools.dao.entity;

import cn.isqing.icloud.common.utils.dto.BaseCondition;
import lombok.Data;

import java.util.List;

@Data
public class CommonTextCondition extends BaseCondition {

    // 关联表主键
    private Long fid;
    // 关联表主键
    private List<Long> fidCondition;

    // 文本类型
    private List<Integer> typeCondition;
    private Integer type;

}
