package cn.isqing.icloud.common.utils.time.ratelimiter;

/**
 * 限流-漏桶算法
 * 线程不安全
 *
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public class LeakyBucketRatelimiterDemo {

    private LeakyBucketRatelimiterDemo() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 每秒处理数（出水率）
     */
    private long rate = 1;

    /**
     * 当前剩余水量
     */
    private long currentWater = 100;

    /**
     * 最后刷新时间
     */
    private long refreshTime;

    /**
     * 桶容量
     */
    private long capacity = 100;

    /**
     * 漏桶算法
     *
     * @return
     */
    boolean tryAcquire() {
        long currentTime = System.nanoTime();
        // 流出的水量 =(当前时间-上次刷新时间)* 出水率
        long outWater = (currentTime - refreshTime) / 1000000000 * rate;
        // 当前水量 = 之前的桶内水量-流出的水量
        currentWater = Math.max(0, currentWater - outWater);
        // 刷新时间
        refreshTime = currentTime;

        // 当前剩余水量还是小于桶的容量，则请求放行
        if (currentWater < capacity) {
            currentWater++;
            return true;
        }
        // 当前剩余水量大于等于桶的容量，限流
        return false;
    }

}
