package cn.isqing.icloud.common.utils.config;

import cn.isqing.icloud.common.api.dto.PageReqDto;
import cn.isqing.icloud.common.utils.enums.ActionType;
import lombok.Builder;
import lombok.Data;

/**
 * 表操作路由配置
 * 定义URL与表操作的映射关系
 *
 * @author songqingwei
 * @version 1.0
 */
@Data
@Builder
public class TableOperationRoute {
    
    /**
     * URL路径（相对于基础路径）
     * 例如: /user/page, /user/insert
     */
    private String path;
    
    /**
     * 表名
     */
    private String tableName;
    
    /**
     * 操作类型
     */
    private ActionType action;
    
    /**
     * 请求DTO类（用于接收前端参数）
     * PAGE_QUERY 时使用 PageReqDto<ConditionClass>
     * INSERT/UPDATE/DELETE 时使用 EntityClass
     */
    private Class<?> requestDtoClass;
    
    /**
     * 响应VO类（用于返回数据）
     */
    private Class<?> responseVoClass;
    
    /**
     * 是否需要登录验证（预留）
     */
    @Builder.Default
    private boolean requireAuth = true;
    
    /**
     * 是否需要权限验证（预留）
     */
    @Builder.Default
    private boolean requirePermission = true;
}
