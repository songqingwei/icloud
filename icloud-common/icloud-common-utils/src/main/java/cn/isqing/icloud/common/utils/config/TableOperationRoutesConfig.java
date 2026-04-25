package cn.isqing.icloud.common.utils.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 表操作路由配置属性
 * 支持在 application.yml 中配置
 *
 * @author songqingwei
 * @version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "icloud.table-operation")
public class TableOperationRoutesConfig {
    
    /**
     * 基础路径，默认为空（无前缀）
     */
    private String basePath = "";
    
    /**
     * 路由配置列表
     */
    private List<TableOperationRoute> routes = new ArrayList<>();
}
