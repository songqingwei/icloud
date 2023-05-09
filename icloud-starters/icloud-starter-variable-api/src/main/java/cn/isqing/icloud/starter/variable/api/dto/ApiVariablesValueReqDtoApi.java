package cn.isqing.icloud.starter.variable.api.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class ApiVariablesValueReqDtoApi extends ApiAuthDto {

    @NotNull
    private String coreId;

    private String inputParams;

    /**
     * 上下文组建运行结果
     * 组件id，json
     */
    private Map<Long, String> aboveResMap = new ConcurrentHashMap<>();

}
