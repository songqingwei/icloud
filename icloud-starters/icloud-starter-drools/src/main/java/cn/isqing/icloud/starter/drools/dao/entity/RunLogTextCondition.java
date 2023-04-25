package cn.isqing.icloud.starter.drools.dao.entity;

import cn.isqing.icloud.common.utils.dto.BaseCondition;
import lombok.Data;

@Data
public class RunLogTextCondition extends BaseCondition {

    private Long id;

    private Integer type;

    private String text;

    private Long fid;

}
