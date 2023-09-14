package cn.isqing.icloud.starter.drools.common.dto;

import lombok.Data;

/**
 * 同步返回结果变量dto
 */
@Data
public class SyncResVariableDto {

    // 返参变量名
    private String name;
    // 对应的变量id
    private Long variableId;
    //
    private String cresPath;

}
