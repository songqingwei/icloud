package cn.isqing.icloud.starter.drools.service.template.dto;

import cn.isqing.icloud.common.utils.validation.group.AddGroup;
import cn.isqing.icloud.common.utils.validation.group.EditGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
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

    // 目标分配比例 json List<Long>
    @NotBlank
    private String targetRatio;

    // 目标名称映射 json map<Long,String>
    @NotBlank
    private String targetName;

    // 分配算法:1固定数量2比例
    @NotNull
    private Integer allocationModel;

    // 规则内容
    @NotBlank
    private String content;

    // 0未启用 1启用
    @NotBlank(message = "版本号不能为空",groups = {EditGroup.class})
    private Integer version;

    // 0未启用 1启用
    private Integer isActive;

    // 关联业务
    @NotEmpty
    private Map<String,String> busiMap;

}
