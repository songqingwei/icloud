package cn.isqing.icloud.starter.variable.dao.entity;

import cn.isqing.icloud.common.utils.dto.BaseCondition;
import lombok.Data;

import java.util.List;

@Data
public class CommonConfigCondition extends BaseCondition {

    private String group;

    private List<String> keyCondition;

    private String key;

}
