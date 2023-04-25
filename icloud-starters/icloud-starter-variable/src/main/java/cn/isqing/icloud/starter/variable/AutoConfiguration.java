package cn.isqing.icloud.starter.variable;

import cn.isqing.icloud.starter.variable.config.MyBeanNameGenerator;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Configuration(value = "iVariableAutoConfiguration")
@ComponentScan(value = "cn.isqing.icloud.starter.variable",nameGenerator = MyBeanNameGenerator.class)
public class AutoConfiguration {

}
