package cn.isqing.icloud.common.utils.config;

import cn.isqing.icloud.common.utils.enums.ActionType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 表操作路由配置示例
 * 
 * 使用方式（二选一）：
 * 1. 在 application.yml 中配置（推荐）
 * 2. 通过Java Config配置（适合动态场景）
 *
 * @author songqingwei
 * @version 1.0
 */
@Configuration
public class TableOperationRouteExampleConfig {

    /**
     * 方式一：在 application.yml 中配置（推荐）
     * 
     * icloud:
     *   table-operation:
     *     base-path: /table-op
     *     routes:
     *       - path: /user/page
     *         table-name: sys_user
     *         action: PAGE_QUERY
     *         request-dto-class: cn.isqing.icloud.dal.condition.SysUserCondition
     *         response-vo-class: cn.isqing.icloud.dal.entity.SysUser
     *       - path: /user/insert
     *         table-name: sys_user
     *         action: INSERT
     *         request-dto-class: cn.isqing.icloud.dal.entity.SysUser
     *       - path: /order/page
     *         table-name: t_order
     *         action: PAGE_QUERY
     *         request-dto-class: cn.isqing.icloud.dal.condition.OrderCondition
     *         response-vo-class: cn.isqing.icloud.dal.entity.Order
     */

    /**
     * 方式二：通过Java Config配置（适合需要动态计算的场景）
     * 如果使用了这种方式，需要在配置类上添加条件注解避免冲突
     */
    /*
    @Bean
    public TableOperationRoutesConfig tableOperationRoutesConfig() {
        TableOperationRoutesConfig config = new TableOperationRoutesConfig();
        config.setBasePath("/table-op");
        
        // 用户管理相关路由
        config.getRoutes().add(TableOperationRoute.builder()
            .path("/user/page")
            .tableName("sys_user")
            .action(ActionType.PAGE_QUERY)
            .requestDtoClass(SysUserCondition.class)
            .responseVoClass(SysUser.class)
            .build());
            
        config.getRoutes().add(TableOperationRoute.builder()
            .path("/user/insert")
            .tableName("sys_user")
            .action(ActionType.INSERT)
            .requestDtoClass(SysUser.class)
            .build());
            
        config.getRoutes().add(TableOperationRoute.builder()
            .path("/user/update")
            .tableName("sys_user")
            .action(ActionType.UPDATE)
            .requestDtoClass(SysUser.class)
            .build());
            
        config.getRoutes().add(TableOperationRoute.builder()
            .path("/user/delete")
            .tableName("sys_user")
            .action(ActionType.DELETE)
            .requestDtoClass(SysUser.class)
            .build());
        
        return config;
    }
    */
}
