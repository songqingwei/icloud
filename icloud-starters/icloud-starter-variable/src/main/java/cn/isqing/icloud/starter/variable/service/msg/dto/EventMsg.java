package cn.isqing.icloud.starter.variable.service.msg.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Data
public class EventMsg {

    @NotNull
    private String id;

    @NotNull
    private String eventType;

    @NotNull
    private List<String> data;

}
