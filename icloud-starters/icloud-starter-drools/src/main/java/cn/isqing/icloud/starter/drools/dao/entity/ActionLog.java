package cn.isqing.icloud.starter.drools.dao.entity;

import lombok.Data;

@Data
public class ActionLog {

    private Long id;

    // 限界id
    private Long coreId;

    // log id
    private Long runLogId;

    // 组件id
    private Long cid;

    private Integer status;

    private Integer failNum;

    private String failReason;

    private Integer version;

}
