package cn.isqing.icloud.starter.variable.service.variable.dto;

import cn.isqing.icloud.common.utils.validation.group.AddGroup;
import cn.isqing.icloud.common.utils.validation.group.EditGroup;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;

@Data
public class VariableDto {

    @Null(message = "添加时主键必须为空",groups = {AddGroup.class})
    @NotNull(message = "主键不能为空",groups = {EditGroup.class})
    private Long id;

    // 变量名
    private String name;

    // component id 组件id
    private Long cid;

    private String cResName;

    private Integer type;

    // 类型：java类路径
    private String typePath;

    // 版本号
    private Integer version;

    private Integer domain;

    private Integer isDel;

    private Integer isActive;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
