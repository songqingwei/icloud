package cn.isqing.icloud.common.utils.dto;

import cn.isqing.icloud.common.utils.enums.ActionType;
import lombok.Data;

/**
 * 通用表操作DTO
 *
 * @author songqingwei
 * @version 1.0
 */
@Data
public class TableOperationDto {

    /**
     * 表名
     */
    private String tableName;

    /**
     * 操作类型
     */
    private ActionType action;

    /**
     * 请求参数(JSON字符串)
     */
    private String req;

    /**
     * 响应vo class
     */
    private Class<?> resVoClass;


}
