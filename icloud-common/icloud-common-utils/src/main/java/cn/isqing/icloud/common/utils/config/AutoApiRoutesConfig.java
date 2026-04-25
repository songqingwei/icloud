package cn.isqing.icloud.common.utils.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 自动API路由配置属性
 * 支持在 application.yml 中配置自动API的前缀
 *
 * @author songqingwei
 * @version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "icloud.auto-api")
public class AutoApiRoutesConfig {
    
    /**
     * 自动API前缀，默认为 /auto-api
     */
    private String prefix = "/auto-api";
}
