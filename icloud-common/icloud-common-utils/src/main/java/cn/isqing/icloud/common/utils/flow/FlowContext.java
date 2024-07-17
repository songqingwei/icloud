package cn.isqing.icloud.common.utils.flow;

import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.common.utils.dao.BaseMapper;
import lombok.Data;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 上下文容器
 *
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Data
public class FlowContext<R> {

    // 静态变量，用于生成唯一值
    private static AtomicLong uniqueValueCounter = new AtomicLong(0);

    // 父类中的唯一属性，使用fuinal 修饰符，保证线程安全
    protected final Long uniqueValue;

    public FlowContext() {
        this.uniqueValue = uniqueValueCounter.incrementAndGet();
    }

    protected long lockTime = 0;

    /**
     * 解锁预占用标识
     */
    protected boolean unCasLockedPre = false;

    protected boolean casLocked = false;

    protected BaseMapper mapper;

    protected Object dataPo;


    /**
     * 本次任务是否结束：中断流程
     */
    protected boolean interrupted = false;
    /**
     * flow end ：flow本身执行完毕 返回之前更新为true
     */
    protected boolean flowEnd = false;
    /**
     * 是否需要打印入参出参
     */
    protected boolean log = true;

    protected Response<R> flowRes = null;

    protected Response<R> flowErrorRes = null;
}
