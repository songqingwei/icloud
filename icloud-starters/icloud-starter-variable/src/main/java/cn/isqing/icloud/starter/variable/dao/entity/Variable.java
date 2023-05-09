package cn.isqing.icloud.starter.variable.dao.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Variable {

    private Long id;

    // 变量名
    private String name;

    // component id 组件id
    private Long cid;

    
    private String cresPath;

    private Integer type;

    // 类型：java类路径
    private String typePath;

    private Long rendererId;

    // 版本号
    private Integer version;

    private Integer domain;

    private Integer isDel;

    private Integer isActive;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String busiCode;

    private String note;

}
