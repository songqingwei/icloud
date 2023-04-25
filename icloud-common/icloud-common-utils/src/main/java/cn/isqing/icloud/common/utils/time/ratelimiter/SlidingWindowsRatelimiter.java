package cn.isqing.icloud.common.utils.time.ratelimiter;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 滑动窗口限流
 *
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public class SlidingWindowsRatelimiter {

    private SlidingWindowsRatelimiter() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 单位时间 60秒
     */
    private static long timeUnit = 60;

    /**
     * 单位时间划分的小周期（单位时间是1分钟，10s一个小格子窗口，一共6个格子）
     */
    private static int subTimeLimt = 10;

    /**
     * 每分钟限流请求数
     */
    private static long numLimit = 100;

    /**
     * 1秒等于1000000000纳秒
     */
    private static long nanoUnit = 1000000000;

    private static final Map<Long, AtomicLong> map = new ConcurrentHashMap<>();

    /**
     * 滑动窗口时间算法实现
     */
    public static boolean tryAcquire() {
        long secondsTime = System.nanoTime() / nanoUnit;
        long windowsIndex = secondsTime / subTimeLimt;
        long windowsIndexStart = (secondsTime - timeUnit) / subTimeLimt;

        AtomicLong num = map.get(windowsIndex);
        if (num == null) {
            map.putIfAbsent(windowsIndex, new AtomicLong());
            num = map.get(windowsIndex);
        }
        long total = 0;
        Iterator<Long> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            Long next = iterator.next();
            if (next < windowsIndexStart) {
                iterator.remove();
                continue;
            }
            if (next <= windowsIndex) {
                total += map.get(next).get();
            }
        }
        if (total > numLimit) {
            return false;
        }
        num.incrementAndGet();

        return true;
    }

}
