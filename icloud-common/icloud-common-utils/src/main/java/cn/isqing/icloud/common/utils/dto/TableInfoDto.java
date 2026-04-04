package cn.isqing.icloud.common.utils.dto;

import cn.isqing.icloud.common.utils.dao.BaseMapper;
import lombok.Data;

/**
 * 表信息DTO
 *
 * @author songqingwei
 * @version 1.0
 */
@Data
public class TableInfoDto {
    /**
     * 实体类Class
     */
    private Class<?> entityClass;

    /**
     * Mapper类
     */
    private BaseMapper mapper;

    /**
     * Condition类Class
     */
    private Class<?> conditionClass;
}
