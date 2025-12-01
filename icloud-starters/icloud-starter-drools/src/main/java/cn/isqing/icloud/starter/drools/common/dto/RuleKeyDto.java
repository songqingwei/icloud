package cn.isqing.icloud.starter.drools.common.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.util.Objects;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Data
public class RuleKeyDto {
    // 域
    @NotNull
    private Integer domain;
    @NotNull
    private String busiCode;

    // 动作
    @NotNull
    private Long actionId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RuleKeyDto that = (RuleKeyDto) o;
        return domain.equals(that.domain) && busiCode.equals(that.busiCode) && actionId.equals(that.actionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(domain, busiCode, actionId);
    }
}
