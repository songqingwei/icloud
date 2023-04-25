package cn.isqing.icloud.common.utils.time.ratelimiter;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 限流-漏桶算法
 * 线程安全
 * 这里认为入水充足
 *
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public class LeakyBucketRatelimiter {

    private LeakyBucketRatelimiter() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 每秒处理数（出水率）
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
    //  * 定时任务 固定速率漏水 这里认为入水充足
    //  */
    // private static ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    //
    // static {
    //     service.scheduleAtFixedRate(()->{
    //         bucket.set(rate);
    //     }, timeUnit, timeUnit, TimeUnit.NANOSECONDS);
    // }


    /**
     * 漏桶算法
     *
     * @return
     */
    public static boolean tryAcquire() {
        //触发漏水
        long nowTime = System.nanoTime();
        if(nowTime-refreshTime>timeUnit && lock.tryLock()){
            if(nowTime-refreshTime>timeUnit){
                refreshTime = nowTime;
                bucket.set(rate);
                lock.unlock();
            }
        }
        //触发漏水结束
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
