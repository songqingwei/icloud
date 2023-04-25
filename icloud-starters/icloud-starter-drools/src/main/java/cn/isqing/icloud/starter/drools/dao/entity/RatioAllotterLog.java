package cn.isqing.icloud.starter.drools.dao.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class RatioAllotterLog {

    private Long id;

    private String uid;

    private LocalDate busiDate;

    private Long coreId;

    // 规则模版id
    private Long rid;

    private Long targetId;

    private BigDecimal refTotal;

    private Long num;

}
