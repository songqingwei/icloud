package cn.isqing.icloud.starter.drools.dao.entity;

import cn.isqing.icloud.common.utils.dto.BaseCondition;
import lombok.Data;

import java.util.List;

@Data
public class ComponentTextCondition extends BaseCondition {

    // 关联表主键
    private List<Long> fidCondition;
    private Long fid;

    private Integer type;
    private List<Integer> typeCondition;

}
