package cn.isqing.icloud.starter.drools.dao.entity;

import lombok.Data;

@Data
public class Action {

    private Long id;

    private String name;

    // 组件id
    private Long cid;

    private Integer version;

    private Integer isActive;

    private Integer isDel;

}
