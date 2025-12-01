package cn.isqing.icloud.starter.admin.config.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author isong
 */
@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {
//    @Value("${i.admin.admin.username:i-admin}")
//    private String username;
//    @Value("${i.admin.admin.password:i-admin-pass}")
//    private String password;

    @Value("${i.admin.web.pre:iadmin}")
    private String pre;
    //添加静态资源处理器
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //设置静态资源的访问路径和存放位置
        registry.addResourceHandler("/"+pre+"/h5/**").addResourceLocations("classpath:/admin/");
    }

    //添加视图控制器
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 将根路径重定向到登录页
        registry.addViewController("/").setViewName("redirect:/"+pre+"/login");
    }

    //添加拦截器
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        //创建拦截器对象
//        MyBasicAuthInterceptor myBasicAuthInterceptor = new MyBasicAuthInterceptor();
//        myBasicAuthInterceptor.setUsername(username);
//        myBasicAuthInterceptor.setPassword(password);
//        //注册拦截器，并设置拦截路径为静态资源路径
//        registry.addInterceptor(myBasicAuthInterceptor).addPathPatterns("/"+pre+"/**");
//    }
}