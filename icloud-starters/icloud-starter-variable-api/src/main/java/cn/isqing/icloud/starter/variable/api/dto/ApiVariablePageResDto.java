package cn.isqing.icloud.starter.variable.api.dto;

import cn.isqing.icloud.common.api.dto.PageResDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiVariablePageResDto<T> extends PageResDto<T> {

    private Map<Long,Map<String,Object>> renderer = Collections.emptyMap();
}
