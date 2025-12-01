package cn.isqing.icloud.starter.drools.service.template.dto;

import cn.isqing.icloud.common.utils.validation.group.AddGroup;
import cn.isqing.icloud.common.utils.validation.group.EditGroup;
import cn.isqing.icloud.starter.drools.common.dto.RuleH5Dto;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import java.util.Map;

@Data
public class RuleTemplateDto {

    @Null(message = "添加时主键必须为空",groups = {AddGroup.class})
    @NotNull(message = "主键不能为空",groups = {EditGroup.class})
    private Long id;

    // 域
    @NotNull
    private Integer domain;

    // 规则模版名称
    private String name;

    // 描述
    private String comment;

    // 动作
    @NotNull
    private Long actionId;

    // cron时间表达式
    @NotBlank
    private String cron;

    // 目标分配比例 json Map<Long, String>
    @NotBlank
    private Map<Long, String> targetRatio;

    // 目标名称映射
    @NotBlank
    private Map<Long, String> targetName;

    // 分配算法:1:固定数量 2:比例 3:高精度比例
    @NotNull
    private Integer allocationModel;

    // 规则内容
    @NotNull
    private RuleH5Dto content;

    private Integer version;

    // 0未启用 1启用
    private Integer isActive;

    // 关联业务
    @NotEmpty
    private Map<String,String> busiMap;

    // 蛇形算法参照物
    private String ref;

}
