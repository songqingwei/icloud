package cn.isqing.icloud.common.utils.flow;

import cn.isqing.icloud.common.api.dto.Response;
import lombok.Data;

/**
 * 上下文容器
 *
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Data
public class FlowContext<R> {

    /**
     * 本次任务是否结束：中断流程
     */
    protected boolean interrupted = false;
    /**
     * flow end ：flow本身执行完毕 返回之前更新为true
     */
    protected boolean flowEnd = false;
    protected boolean casLock = false;
    /**
     * 是否需要打印入参出参
     */
    protected boolean log = true;

    protected Response<R> flowRes = null;

    protected Response<R> flowErrorRes = null;
}
