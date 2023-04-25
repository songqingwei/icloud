package cn.isqing.icloud.common.utils.time.ratelimiter;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 限流-令牌桶算法
 * 线程安全
 *
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public class TokenBucketRatelimiter {

    private TokenBucketRatelimiter() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 每秒处理数（令牌生成率）
     */
    private static long rate = 20;
    private static long timeUnit = 1000000000;

    /**
     * 桶容量
     */
    private static long capacity = 100;

    private static final AtomicLong bucket = new AtomicLong();

    private static long refreshTime = System.nanoTime();

    private static Lock lock = new ReentrantLock();

    // /**
    //  * 定时任务 固定速率生成令牌
    //  */
    // private static ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    //
    // static {
    //     service.scheduleAtFixedRate(()->{
    //         long now = bucket.get();
    //         if(now+rate>capacity){
    //             bucket.set(capacity-now);
    //             return;
    //         }
    //         bucket.set(rate);
    //     }, timeUnit, timeUnit, TimeUnit.NANOSECONDS);
    // }


    /**
     * 漏桶算法
     *
     * @return
     */
    public static boolean tryAcquire() {
        //触发生成令牌
        long nowTime = System.nanoTime();
        try {
            if(nowTime-refreshTime>timeUnit && lock.tryLock(1,TimeUnit.SECONDS)){
                if(nowTime-refreshTime>timeUnit){
                    refreshTime = nowTime;
                    long tokenNum = (nowTime - refreshTime) / timeUnit * rate;
                    long now = bucket.get();
                    if(now+tokenNum>capacity){
                        bucket.set(capacity-now);
                    }else {
                        bucket.set(tokenNum);
                    }
                }
            }
        } catch (InterruptedException e) {
            return false;
        }
        //触发生成令牌
        if (bucket.get() <= 0) {
            return false;
        }
        long l = bucket.decrementAndGet();
        if (l >= 0) {
            return true;
        }
        return false;
    }



}
