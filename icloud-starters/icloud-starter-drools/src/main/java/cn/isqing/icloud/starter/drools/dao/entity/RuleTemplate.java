package cn.isqing.icloud.starter.drools.dao.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RuleTemplate {

    private Long id;

    // 域
    private Integer domain;

    // 规则模版名称
    private String name;

    // 描述
    private String comment;

    // 动作
    private Long actionId;

    // cron时间表达式
    private String cron;

    // 分配算法:1固定数量 2比例 3高精度比例
    private Integer allocationModel;

    // 0未启用 1启用
    private Integer isActive;

    private Integer version;

    private Long orgId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer isDel;

    // 蛇形算法参照物:变量id
    private Long refId;

}
