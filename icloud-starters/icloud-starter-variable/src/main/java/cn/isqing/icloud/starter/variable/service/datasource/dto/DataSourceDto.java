package cn.isqing.icloud.starter.variable.service.datasource.dto;


import cn.isqing.icloud.common.utils.validation.group.AddGroup;
import cn.isqing.icloud.common.utils.validation.group.EditGroup;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Map;

@Data
public class DataSourceDto {
    @Null(message = "添加时主键必须为空",groups = {AddGroup.class})
    @NotNull(message = "主键不能为空",groups = {EditGroup.class})
    private Long id;

    private String name;

    // 类型 1数据库 2 dubbo
    private Integer type;

    // 类型 1数据库 2 dubbo
    private Integer isActive;

    private Map<String,Object> config;

}
