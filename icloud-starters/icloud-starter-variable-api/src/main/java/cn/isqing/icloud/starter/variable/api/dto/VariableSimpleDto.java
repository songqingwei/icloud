package cn.isqing.icloud.starter.variable.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VariableSimpleDto {

    private Long id;
    // component id 组件id
    private Long cid;

    
    private String cresPath;

}
