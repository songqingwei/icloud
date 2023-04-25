package cn.isqing.icloud.starter.drools.dao.entity;

import lombok.Data;

@Data
public class RuleCore {

    private Long id;

    // 域
    private Integer domain;

    // 动作
    private Long actionId;

    // 业务编码
    private String busiCode;

    private Long version;

}
