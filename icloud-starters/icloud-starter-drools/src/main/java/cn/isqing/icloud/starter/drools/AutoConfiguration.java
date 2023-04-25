package cn.isqing.icloud.starter.drools;

import cn.isqing.icloud.starter.drools.config.MyBeanNameGenerator;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Configuration(value = "iDroolsAutoConfiguration")
@ComponentScan(value = "cn.isqing.icloud.starter.drools", nameGenerator = MyBeanNameGenerator.class)
public class AutoConfiguration {
}
