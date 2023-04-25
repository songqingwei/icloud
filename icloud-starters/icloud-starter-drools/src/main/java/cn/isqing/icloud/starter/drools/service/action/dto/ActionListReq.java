package cn.isqing.icloud.starter.drools.service.action.dto;

import lombok.Data;

@Data
public class ActionListReq {

    private Long id;

    private Integer isActive;

    private String nameConditionLike;

    private Long cid;

}
