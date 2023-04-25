package cn.isqing.icloud.common.utils.kit;

import cn.isqing.icloud.common.utils.dao.BaseMapper;
import cn.isqing.icloud.common.utils.enums.status.YesOrNo;
import cn.isqing.icloud.common.utils.flow.FlowContext;
import cn.isqing.icloud.common.utils.time.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Component
@Slf4j
public class LockUtil {

    private static final Map<FlowContext, Map<Object, BaseMapper>> DO_LOCK_MAP = new ConcurrentHashMap<>();

    private static final ThreadPoolTaskExecutor jobRxecutor = new ThreadPoolTaskExecutor();

    static {
        jobRxecutor.setThreadNamePrefix("do-watchdog");
        jobRxecutor.setMaxPoolSize(120);
        jobRxecutor.setCorePoolSize(64);
        jobRxecutor.setQueueCapacity(600);
        jobRxecutor.setAllowCoreThreadTimeOut(true);
        jobRxecutor.setKeepAliveSeconds(300);
        jobRxecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
    }


    private static final ScheduledExecutorService excutor = Executors.newSingleThreadScheduledExecutor();

    static {
        excutor.scheduleAtFixedRate(() -> {
            Iterator<Map.Entry<FlowContext, Map<Object, BaseMapper>>> iterator = DO_LOCK_MAP.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<FlowContext, Map<Object, BaseMapper>> next = iterator.next();
                jobRxecutor.submit(() -> {
                    if (next.getKey().isFlowEnd()) {
                        DO_LOCK_MAP.remove(next.getKey());
                        return;
                    }
                    Iterator<Map.Entry<Object, BaseMapper>> iterator1 = next.getValue().entrySet().iterator();
                    while (iterator1.hasNext()) {
                        Map.Entry<Object, BaseMapper> next1 = iterator1.next();
                        synchronized (next1.getKey()) {
                            if (next.getValue().containsKey(next1.getKey())) {
                                try {
                                    renewalDo(next1.getKey(), next1.getValue());
                                } catch (Exception e) {
                                    log.error(e.getMessage(), e);
                                }
                            }
                        }
                    }
                });
            }
        }, 5, 5, TimeUnit.MINUTES);
    }


    public static RedissonClient redissonClient;

    @Autowired
    public void setRedissonClient(RedissonClient redissonClient) {
        LockUtil.redissonClient = redissonClient;
    }

    public static RLock getRedisLock(String key) {
        return getRedisLock(key, 5, TimeUnit.SECONDS);
    }


    public static void tryRunWithRLock(String key, long time, TimeUnit unit, Predicate predicate, Consumer consumer) {
        RLock rLock = getRedisLock(key, time, unit);
        if (rLock == null) {
            log.info("未获取到锁取消执行");
            return;
        }
        try {
            boolean test = predicate.test(null);
            if(test){
                consumer.accept(null);
            }
        } finally {
            try {
                rLock.unlock();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    public static RLock getRedisLock(String key, long time, TimeUnit unit) {
        RLock lock = redissonClient.getLock(key);
        try {
            boolean locked = lock.tryLock(time, unit);
            if (locked) {
                return lock;
            }
        } catch (InterruptedException e) {
            log.warn(e.getMessage(), e);
            // 表示为当前线程打中断标记:这里是代码扫描要求不能忽视中断异常
            Thread.currentThread().interrupt();
            // Thread.interrupted()能告诉你线程是否发生中断,并将清除中断状态标记
            // 这里清除中断标记是为了让后续逻辑正常，中断表示我们没有获取到锁而已，至于谁通知我们中断的 暂时忽略
            Thread.interrupted();
        }
        return null;
    }


    public static boolean lockPo(FlowContext context, Object dataPo, BaseMapper mapper) throws Exception {
        Class<?> aClass = dataPo.getClass();
        Method getLockStatus = aClass.getMethod("getLockStatus");
        Method getLockTime = aClass.getMethod("getLockTime");
        if (getLockStatus.invoke(dataPo).equals(YesOrNo.YES.ordinal()) && ((LocalDateTime) getLockTime.invoke(dataPo)).isAfter(TimeUtil.now())) {
            return false;
        }
        Method setLockVersion = aClass.getMethod("setLockVersion", Long.class);
        Method getLockVersion = aClass.getMethod("getLockVersion");
        int lock = mapper.lock(dataPo);
        if (lock > 0) {
            setLockVersion.invoke(dataPo, ((long) getLockVersion.invoke(dataPo)) + 1L);
            Map<Object, BaseMapper> mapperMap = DO_LOCK_MAP.computeIfAbsent(context,
                    k -> new ConcurrentHashMap<>());
            mapperMap.put(dataPo, mapper);
            return true;
        }
        return false;
    }

    public static boolean unlockPo(FlowContext context, Object dataPo, BaseMapper mapper) {
        Class<?> aClass = dataPo.getClass();
        Method setLockVersion;
        Method getLockVersion;
        try {
            setLockVersion = aClass.getMethod("setLockVersion", Long.class);
            getLockVersion = aClass.getMethod("getLockVersion");
        } catch (NoSuchMethodException e) {
            log.error(e.getMessage(), e);
            return false;
        }
        int lock = mapper.unlock(dataPo);
        boolean res = false;
        if (lock > 0) {
            try {
                setLockVersion.invoke(dataPo, ((long) getLockVersion.invoke(dataPo)) + 1L);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            res = true;
        }
        Map<Object, BaseMapper> map = DO_LOCK_MAP.get(context);
        synchronized (dataPo) {
            map.remove(dataPo);
        }
        if (map.isEmpty()) {
            DO_LOCK_MAP.remove(context);
        }
        return res;
    }

    public static boolean renewalDo(Object dataDo, BaseMapper mapper) throws Exception {
        Class<?> aClass = dataDo.getClass();
        Method setLockVersion = aClass.getMethod("setLockVersion", Long.class);
        Method getLockVersion = aClass.getMethod("getLockVersion");
        int lock = mapper.lock(dataDo);
        if (lock > 0) {
            setLockVersion.invoke(dataDo, ((long) getLockVersion.invoke(dataDo)) + 1L);
            return true;
        }
        return false;
    }

}
