package cn.isqing.icloud.starter.drools.dao.entity;

import cn.isqing.icloud.common.utils.dto.BaseCondition;
import lombok.Data;

import java.util.List;

@Data
public class ComponentCondition extends BaseCondition {

    private List<Long> idConditon;

    private Integer isDel;

}
