package cn.isqing.icloud.starter.variable.service.component.dto;

import cn.isqing.icloud.common.utils.validation.group.AddGroup;
import cn.isqing.icloud.common.utils.validation.group.EditGroup;
import cn.isqing.icloud.starter.variable.common.constants.ComponentTextTypeConstants;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Map;
import java.util.Set;

@Data
public class ComponentDto {
    @Null(message = "添加时主键必须为空",groups = {AddGroup.class})
    @NotNull(message = "主键不能为空",groups = {EditGroup.class})
    private Long id;

    private String name;

    private Integer dataSourcetype;

    private Long dataSourceId;

    //方言
    private String dialectConfig;

    private Set<Long> dependCids;

    private Map<String,String> dependInputParams;

    private Map<String,String> dependCRes;

    private Map<String,String>  dependConstantParams;
    private Map<String,String>  selfConstants;

    private Map<String,String>  dependSystemVars;

    private String[] resJudge;

}
