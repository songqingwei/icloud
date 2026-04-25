package cn.isqing.icloud.common.utils.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 表操作路由Web配置
 * 注册拦截器到Spring MVC
 *
 * @author songqingwei
 * @version 1.0
 */
@Slf4j
@Configuration
public class TableOperationWebConfig implements WebMvcConfigurer {

    @Autowired
    private TableOperationRouteInterceptor tableOperationRouteInterceptor;

    @Autowired
    private AutoApiRouteInterceptor autoApiRouteInterceptor;

    @Autowired
    private AutoApiRoutesConfig autoApiConfig;

    @Autowired
    private TableOperationRoutesConfig routesConfig;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("=== 开始注册表操作拦截器 ===");
        
        // 注册自动API路由拦截器（零配置，智能推断）
        // 根据配置动态构建拦截路径模式
        String autoApiPrefix = autoApiConfig.getPrefix();
        String autoApiPattern = (autoApiPrefix == null || autoApiPrefix.isEmpty()) ? "/**" : autoApiPrefix + "/**";
        
        registry.addInterceptor(autoApiRouteInterceptor)
                .addPathPatterns(autoApiPattern)
                .excludePathPatterns(
                    "/static/**",
                    "/public/**",
                    "/resources/**",
                    "/css/**",
                    "/js/**",
                    "/images/**"
                );
        log.info("已注册自动API拦截器: {}", autoApiPattern);
        
        // 注册配置式表操作路由拦截器
        // 根据配置动态构建拦截路径模式
        String basePath = routesConfig.getBasePath();
        String tableOpPattern = (basePath == null || basePath.isEmpty()) ? "/**" : basePath + "/**";
        
        registry.addInterceptor(tableOperationRouteInterceptor)
                .addPathPatterns(tableOpPattern)
                .excludePathPatterns(
                    "/static/**",
                    "/public/**",
                    "/resources/**",
                    "/css/**",
                    "/js/**",
                    "/images/**"
                );
        log.info("已注册配置式路由拦截器: {}", tableOpPattern);
        
        log.info("=== 表操作拦截器注册完成 ===");
    }
}
