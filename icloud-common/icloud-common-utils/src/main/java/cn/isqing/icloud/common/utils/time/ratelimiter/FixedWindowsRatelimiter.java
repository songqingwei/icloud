package cn.isqing.icloud.common.utils.time.ratelimiter;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 限流
 *
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public class FixedWindowsRatelimiter {

    private  FixedWindowsRatelimiter() {
        throw new IllegalStateException("Utility class");
    }

    private static long startTime = System.nanoTime();
    //1秒 单位：纳秒
    private static volatile long timelimit = 1000000000;
    private static long numLimit = 10;
    private static AtomicLong num = new AtomicLong(0);

    /**
     * 固定窗口时间算法
     * 时间窗口和自然秒不对应
     *
     * @return
     */
    public static boolean tryAcquire() {
        long currentTime = System.nanoTime();
        if (currentTime - startTime > timelimit) {
            synchronized (num) {
                if (startTime != currentTime) {
                    startTime = currentTime;
                    num.set(0);
                }
            }
        }
        if (num.get() < numLimit) {
            num.incrementAndGet();
            return true;
        }
        return false;
    }


}
