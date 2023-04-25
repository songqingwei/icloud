package cn.isqing.icloud.starter.variable.dao.entity;

import cn.isqing.icloud.common.utils.dto.BaseCondition;
import lombok.Data;

import java.util.List;

@Data
public class CommonConfigCondition extends BaseCondition {

    private Integer group;

    private List<String> keyCondtion;

    private String key;

}
