package cn.isqing.icloud.starter.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "cn.isqing.icloud.starter.admin")
@MapperScan("cn.isqing.icloud.starter.admin.dao.mapper")
@ConditionalOnProperty(prefix = "i.admin", name = "enabled", havingValue = "true", matchIfMissing = true)
public class AutoConfiguration {
}