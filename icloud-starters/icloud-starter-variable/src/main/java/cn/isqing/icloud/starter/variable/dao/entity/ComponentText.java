package cn.isqing.icloud.starter.variable.dao.entity;

import lombok.Data;

@Data
public class ComponentText {

    private Long id;

    // 关联表主键
    private Long fid;

    // 文本
    private String text;

    // 文本类型
    private Integer type;

}
