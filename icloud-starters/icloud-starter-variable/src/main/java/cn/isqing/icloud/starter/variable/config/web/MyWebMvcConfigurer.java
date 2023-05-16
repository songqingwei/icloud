package cn.isqing.icloud.starter.variable.config.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author isong
 */
@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {
    @Value("${i.variable.admin.username:i-admin}")
    private String username;
    @Value("${i.variable.admin.password:i-admin-pass}")
    private String password;

    @Value("${i.variable.web.pre:ivariable}")
    private String pre;
    //添加静态资源处理器
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //设置静态资源的访问路径和存放位置
        registry.addResourceHandler("/"+pre+"/h5/**").addResourceLocations("classpath:/ivariable/");
    }

    //添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //创建拦截器对象
        MyBasicAuthInterceptor myBasicAuthInterceptor = new MyBasicAuthInterceptor();
        myBasicAuthInterceptor.setUsername(username);
        myBasicAuthInterceptor.setPassword(password);
        //注册拦截器，并设置拦截路径为静态资源路径
        registry.addInterceptor(myBasicAuthInterceptor).addPathPatterns("/"+pre+"/**");
    }
}
