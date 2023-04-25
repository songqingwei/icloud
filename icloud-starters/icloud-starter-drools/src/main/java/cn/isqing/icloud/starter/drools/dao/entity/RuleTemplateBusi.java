package cn.isqing.icloud.starter.drools.dao.entity;

import lombok.Data;

@Data
public class RuleTemplateBusi {

    private Long id;

    // 模版id
    private Long tid;

    // 业务编码
    private String busiCode;

    private Integer version;

    private String busiName;

}
