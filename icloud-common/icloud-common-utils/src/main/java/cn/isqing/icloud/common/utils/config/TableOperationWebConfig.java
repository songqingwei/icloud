package cn.isqing.icloud.common.utils.config;

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
@Configuration
public class TableOperationWebConfig implements WebMvcConfigurer {

    @Autowired
    private TableOperationRouteInterceptor tableOperationRouteInterceptor;

    @Autowired
    private AutoApiRouteInterceptor autoApiRouteInterceptor;

    @Autowired
    private TableOperationRoutesConfig routesConfig;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册自动API路由拦截器（零配置，智能推断）
        // 拦截 /auto-api/** 路径
        registry.addInterceptor(autoApiRouteInterceptor)
                .addPathPatterns("/auto-api/**")
                .order(1); // 设置优先级，确保在其他拦截器之前执行
        
        // 注册配置式表操作路由拦截器
        // 拦截所有配置的basePath下的路径
        String basePath = routesConfig.getBasePath() + "/**";
        registry.addInterceptor(tableOperationRouteInterceptor)
                .addPathPatterns(basePath)
                .order(2); // 优先级稍低
        
        // 这样配置的拦截器会被Spring完整管理：
        // 1. 会经过所有已注册的Filter
        // 2. 会经过其他Interceptor（如日志、权限验证等）
        // 3. 支持事务管理
        // 4. 完整的Spring生命周期
    }
}
