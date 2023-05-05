package cn.isqing.icloud.common.utils;

import cn.isqing.icloud.common.utils.kit.ParallelStreamUtil;
import cn.isqing.icloud.common.utils.log.MDCUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.concurrent.ForkJoinPool;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Configuration("iCommonUtilsAutoConfiguration")
@ComponentScan("cn.isqing.icloud.common.utils")
@ConfigurationProperties(prefix = "i.common")
public class AutoConfiguration {

    private String traceIdField;

    private Integer forkJoinPoolNum;

    public void setTraceIdField(String traceIdField) {
        this.traceIdField = traceIdField;
        if(!StringUtils.isEmpty(traceIdField)){
            MDCUtil.setTraceIdField(traceIdField);
        }
    }
    public void setForkJoinPool(Integer num) {
        this.forkJoinPoolNum  = num;
        ParallelStreamUtil.forkJoinPool = new ForkJoinPool(num);
    }

}
