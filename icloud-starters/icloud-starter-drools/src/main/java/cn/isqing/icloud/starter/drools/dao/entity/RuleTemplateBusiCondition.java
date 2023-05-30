package cn.isqing.icloud.starter.drools.dao.entity;

import cn.isqing.icloud.common.utils.dto.BaseCondition;
import lombok.Data;

import java.util.List;

@Data
public class RuleTemplateBusiCondition extends BaseCondition {

    private List<Long> tidCondition;

    private Long tid;

    // 关联业务
    private List<String> busiCode;
}
