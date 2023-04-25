package cn.isqing.icloud.starter.variable.service.variable.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class VariableActionsDto {

    @NotNull(message = "主键不能为空")
    private Long id;

    private List<Long> actions;

}
