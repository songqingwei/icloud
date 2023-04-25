package cn.isqing.icloud.starter.drools.service.msg.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Data
public class EventMsg {

    private Long id;

    @NotNull
    private String eventType;

    private List<String> data;

}
