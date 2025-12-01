package cn.isqing.icloud.starter.drools.service.msg.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Data
public class TplChangeMsg {

    // 域
    @NotNull
    private Integer domain;
    @NotNull
    private String busiCode;
    // 动作
    @NotNull
    private Long actionId;

    // 消息生产时间
    private LocalDateTime createTime;

}
