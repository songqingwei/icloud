package cn.isqing.icloud.starter.variable.service.component.dto;

import lombok.Data;

import java.util.Map;
import java.util.Set;

@Data
public class ComponentListReq {

    private Long id;

    private String nameConditionLike;

    private Integer isActive;

    private Integer dataSourceType;

    private Long dataSourceId;

    public Set<String> dependInputParams;
    // map<cid,res_name>
    public Map<String,String> dependCRes;
}
