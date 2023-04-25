package cn.isqing.icloud.starter.drools.service.scheduler.job;

import cn.isqing.icloud.starter.drools.service.semaphore.util.SingleRatioAllotter;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Component
@Slf4j
public class ClearAllotter {

    @XxlJob(value = "clearAllotter")
    public ReturnT<String> exec(String s){
        SingleRatioAllotter.clear();
        return ReturnT.SUCCESS;
    }
}
