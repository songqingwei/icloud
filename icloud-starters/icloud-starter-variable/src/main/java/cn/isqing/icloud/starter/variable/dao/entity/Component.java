package cn.isqing.icloud.starter.variable.dao.entity;

import lombok.Data;

@Data
public class Component {

    private Long id;

    private String name;

    private Long dataSourceId;

    private Integer dataSourceType;

    private Integer version;

    private Integer isActive;

    private Integer isDel;

}
