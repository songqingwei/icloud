package cn.isqing.icloud.starter.drools.service.component.dto;

import cn.isqing.icloud.common.utils.validation.group.AddGroup;
import cn.isqing.icloud.common.utils.validation.group.EditGroup;
import cn.isqing.icloud.starter.drools.common.constants.ComponentTextTypeConstants;
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

    public Set<String> dependInputParams;
    // Set<c_res_name>
    public Set<String> dependCResName;

    /**
     * key:{@link ComponentTextTypeConstants}
     */
    private Map<Integer,Object> dialectConfig;

    private Map<String,String>  constantParams;
    private Map<String,String>  variableParams;
    private String[] resJudge;

}
