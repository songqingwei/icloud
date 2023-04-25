package cn.isqing.icloud.starter.variable.common.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Data
public class UpdateStatusDto {

    @NotNull
    private Long id;
    @NotNull
    private Integer status;
    @NotNull
    private Integer version;

}
