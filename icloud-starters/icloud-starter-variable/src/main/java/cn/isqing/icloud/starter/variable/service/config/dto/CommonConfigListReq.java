package cn.isqing.icloud.starter.variable.service.config.dto;

import lombok.Data;

@Data
public class CommonConfigListReq {

    private Long id;

    private String group;

    private String keyConditionLike;

    private String value;

}
