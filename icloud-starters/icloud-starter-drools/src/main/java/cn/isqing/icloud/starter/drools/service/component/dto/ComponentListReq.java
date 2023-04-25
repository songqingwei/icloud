package cn.isqing.icloud.starter.drools.service.component.dto;

import lombok.Data;

import java.util.Map;
import java.util.Set;

@Data
public class ComponentListReq {

    private Long id;

    private String nameConditionLike;

    private Integer isActive;

    private Integer dataSourcetype;

    private Long dataSourceId;

    public Set<String> dependInputParams;
    // map<cid,res_name>
    public Map<String,String> dependCRes;
}
