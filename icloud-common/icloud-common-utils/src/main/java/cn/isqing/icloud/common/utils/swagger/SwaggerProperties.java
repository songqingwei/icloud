package cn.isqing.icloud.common.utils.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Swagger配置属性
 * 支持在 application.yml 中配置Swagger相关参数
 *
 * @author songqingwei
 * @version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "icloud.swagger")
public class SwaggerProperties {
    
    /**
     * 是否启用Swagger，默认false（生产环境建议关闭）
     */
    private boolean enabled = false;
    
    /**
     * API文档标题
     */
    private String title = "iCloud API Documentation";
    
    /**
     * API文档描述
     */
    private String description = "iCloud Common Utils API Documentation";
    
    /**
     * API版本号
     */
    private String version = "1.0.0";
    
    /**
     * 服务器URL
     */
    private String serverUrl = "";
    
    /**
     * 联系人名称
     */
    private String contactName = "iCloud Team";
    
    /**
     * 联系人邮箱
     */
    private String contactEmail = "support@isqing.cn";
    
    /**
     * 联系人URL
     */
    private String contactUrl = "";
    
    /**
     * 是否生成auto-api文档，默认true
     */
    private boolean autoApiEnabled = true;
    
    /**
     * 是否生成配置式API文档，默认true
     */
    private boolean tableOperationApiEnabled = true;
}
