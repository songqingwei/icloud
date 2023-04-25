package cn.isqing.icloud.common.utils.time.ratelimiter;

import lombok.Data;

/**
 * 限流-令牌桶算法
 * 线程不安全
 *
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Data
public class TokenBucketRatelimiterDemo {

    /**
     * 每秒处理数（放入令牌数量）
     */
    private long putTokenRate;

    /**
     * 最后刷新时间
     */
    private long refreshTime;

    /**
     * 令牌桶容量
     */
    private long capacity;

    /**
     * 当前桶内令牌数
     */
    private long currentToken = 0L;

    /**
     * 漏桶算法
     *
     * @return
     */
    boolean tokenBucketTryAcquire() {
        long currentTime = System.currentTimeMillis();  //获取系统当前时间
        long generateToken = (currentTime - refreshTime) / 1000 * putTokenRate; //生成的令牌 =(当前时间-上次刷新时间)* 放入令牌的速率
        currentToken = Math.min(capacity, generateToken + currentToken); // 当前令牌数量 = 之前的桶内令牌数量+放入的令牌数量
        refreshTime = currentTime; // 刷新时间

        //桶里面还有令牌，请求正常处理
        if (currentToken > 0) {
            currentToken--; //令牌数量-1
            return true;
        }

        return false;
    }

}
