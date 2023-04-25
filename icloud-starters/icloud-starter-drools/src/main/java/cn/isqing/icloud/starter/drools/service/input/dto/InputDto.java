package cn.isqing.icloud.starter.drools.service.input.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Data
public class InputDto implements Serializable {
    // 域
    @NotNull
    private Integer domain;
    @NotBlank
    private String busiCode;
    // 动作
    @NotNull
    private Long actionId;
    // 业务记录主键标识
    @NotNull
    private Long sourceId;

    @NotBlank
    private String params;

}
