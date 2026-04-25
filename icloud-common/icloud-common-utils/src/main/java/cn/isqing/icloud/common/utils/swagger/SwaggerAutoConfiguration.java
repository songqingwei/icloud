package cn.isqing.icloud.common.utils.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger自动配置类
 * 仅在以下两个条件都满足时加载:
 * 1. classpath中存在SpringDoc
 * 2. icloud.swagger.enabled=true
 *
 * @author songqingwei
 * @version 1.0
 */
@Slf4j
@Configuration
@ConditionalOnClass(name = "org.springdoc.core.configuration.SpringDocConfiguration")
@ConditionalOnProperty(prefix = "icloud.swagger", name = "enabled", havingValue = "true")
public class SwaggerAutoConfiguration {

    @Autowired
    private SwaggerProperties swaggerProperties;

    @Autowired
    private AutoApiSwaggerGenerator swaggerGenerator;

    /**
     * 创建OpenAPI Bean，配置基本信息
     */
    @Bean
    public OpenAPI customOpenAPI() {
        log.info("=== Swagger已启用 ===");
        log.info("标题: {}", swaggerProperties.getTitle());
        log.info("描述: {}", swaggerProperties.getDescription());
        log.info("版本: {}", swaggerProperties.getVersion());

        OpenAPI openAPI = new OpenAPI();

        // 设置基本信息
        Info info = new Info()
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .version(swaggerProperties.getVersion());

        // 设置联系人信息
        Contact contact = new Contact()
                .name(swaggerProperties.getContactName())
                .email(swaggerProperties.getContactEmail());
        
        if (swaggerProperties.getContactUrl() != null && !swaggerProperties.getContactUrl().isEmpty()) {
            contact.url(swaggerProperties.getContactUrl());
        }
        
        info.contact(contact);
        openAPI.info(info);

        // 设置服务器URL（如果配置了）
        if (swaggerProperties.getServerUrl() != null && !swaggerProperties.getServerUrl().isEmpty()) {
            Server server = new Server();
            server.setUrl(swaggerProperties.getServerUrl());
            openAPI.addServersItem(server);
        }

        log.info("=== Swagger配置完成 ===");
        return openAPI;
    }

    /**
     * 创建全局OpenAPI定制器，动态添加auto-api和配置式API文档
     */
    @Bean
    public GlobalOpenApiCustomizer globalOpenApiCustomizer() {
        return openApi -> {
            log.info("开始自定义OpenAPI文档...");

            // 生成并合并auto-api路径
            if (swaggerProperties.isAutoApiEnabled()) {
                var autoApiPaths = swaggerGenerator.generateAutoApiPaths();
                if (openApi.getPaths() == null) {
                    openApi.paths(autoApiPaths);
                } else {
                    openApi.getPaths().putAll(autoApiPaths);
                }
                log.info("已合并 {} 个auto-api路径", autoApiPaths.size());
            }

            // 生成并合并配置式API路径
            if (swaggerProperties.isTableOperationApiEnabled()) {
                var tableOpPaths = swaggerGenerator.generateTableOperationPaths();
                if (openApi.getPaths() == null) {
                    openApi.paths(tableOpPaths);
                } else {
                    openApi.getPaths().putAll(tableOpPaths);
                }
                log.info("已合并 {} 个配置式API路径", tableOpPaths.size());
            }

            log.info("OpenAPI文档自定义完成");
        };
    }
}
