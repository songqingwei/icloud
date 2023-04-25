package cn.isqing.icloud.starter.variable.dao.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Variable {

    private Long id;

    // 变量名
    private String name;

    // component id 组件id
    private Long cid;

    private String cResPath;

    private Integer type;

    // 类型：java类路径
    private String typePath;

    // 版本号
    private Integer version;

    private Integer domain;

    private Integer isDel;

    private Integer isActive;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}