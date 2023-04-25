package cn.isqing.icloud.starter.drools.dao.entity;

import lombok.Data;

@Data
public class CommonText {

    private Long id;

    // 关联表主键
    private Long fid;

    // 文本
    private String text;

    // 文本类型
    private Integer type;

}
