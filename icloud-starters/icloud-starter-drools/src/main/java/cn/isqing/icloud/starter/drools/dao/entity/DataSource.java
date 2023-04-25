package cn.isqing.icloud.starter.drools.dao.entity;

import lombok.Data;

@Data
public class DataSource {

    private Long id;

    private String name;

    // 类型 1数据库 2 dubbo
    private Integer type;

    private Integer version;

    private Integer isActive;

}
