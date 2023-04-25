package cn.isqing.icloud.starter.drools.common.dto;

import cn.isqing.icloud.starter.drools.common.enums.OperatorType;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 多维规则条件
 * 每个维度中 and/or 必须是统一的
 *
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Data
public class RuleH5Dto implements Serializable {

    // and/or
    private String relation;

    /**
     * 条件组：RuleH5Dto[继续套娃]
     * list有元素时，改属性值会被忽略
      */
    private List<RuleH5Dto> grouplist;

    /**
     * 条件组：RuleFieldDto[结束套娃]
      */
    private List<RuleFieldDto> list;

    @Data
    public static class RuleFieldDto implements Serializable {
        /**
         * 比较操作符 {@link OperatorType}
         */
        private Long operator;
        // 比较值
        private String value;

        // 变量id
        private Long id;

    }

}
