package cn.isqing.icloud.common.utils;

import cn.isqing.icloud.common.utils.kit.ParallelStreamUtil;
import cn.isqing.icloud.common.utils.log.MDCUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import jakarta.annotation.PostConstruct;
import java.util.concurrent.ForkJoinPool;

/**
 * iCloud Common Utils 自动配置类
 * 
 * 注意: 此类通过 META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports 自动加载
 * 如果未自动加载,请检查:
 * 1. 该文件是否正确配置了此类的完整类名
 * 2. 项目是否正确引入了 icloud-common-utils 依赖
 * 3. Spring Boot 版本是否为 2.7+ 或 3.x (支持新的自动配置机制)
 *
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Slf4j
@Configuration("iCommonUtilsAutoConfiguration")
@ComponentScan(basePackages = "cn.isqing.icloud.common.utils")
@ConfigurationProperties(prefix = "i.common")
@ConditionalOnProperty(prefix = "i.common", name = "enabled", havingValue = "true", matchIfMissing = true)
public class AutoConfiguration {

    @PostConstruct
    public void init() {
        log.info("=== iCommonUtilsAutoConfiguration 已加载 ===");
        log.info("TraceId Field: {}", traceIdField);
        log.info("ForkJoinPool Size: {}", forkJoinPool);
    }


    private String traceIdField;

    private Integer forkJoinPool;

    public void setTraceIdField(String traceIdField) {
        this.traceIdField = traceIdField;
        if(!StringUtils.isEmpty(traceIdField)){
            MDCUtil.setTraceIdField(traceIdField);
            log.info("MDC TraceId Field 已设置为: {}", traceIdField);
        }
    }
    
    public void setForkJoinPool(Integer forkJoinPool) {
        this.forkJoinPool = forkJoinPool;
        if (forkJoinPool != null && forkJoinPool > 0) {
            ParallelStreamUtil.forkJoinPool = new ForkJoinPool(forkJoinPool);
            log.info("ForkJoinPool 已初始化,线程数: {}", forkJoinPool);
        }
    }

}
