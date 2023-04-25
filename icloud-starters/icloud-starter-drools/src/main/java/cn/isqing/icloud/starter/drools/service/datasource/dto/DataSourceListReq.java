package cn.isqing.icloud.starter.drools.service.datasource.dto;

import lombok.Data;

@Data
public class DataSourceListReq {

    private Long id;

    private Integer isActive;

    private String nameConditionLike;

    // 类型 1数据库 2 dubbo
    private Integer type;

}
