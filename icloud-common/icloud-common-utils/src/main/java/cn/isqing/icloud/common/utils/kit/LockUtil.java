package cn.isqing.icloud.common.utils.kit;

import cn.isqing.icloud.common.utils.dao.BaseMapper;
import cn.isqing.icloud.common.utils.enums.status.YesOrNo;
import cn.isqing.icloud.common.utils.flow.FlowContext;
import cn.isqing.icloud.common.utils.time.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.lang.reflect.Method;
import java.time.Instant;
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

    private static final Map<Long, FlowContext> DO_LOCK_MAP = new ConcurrentHashMap<>();

    private static ScheduledExecutorService scheduledExecutor;

    // Spring 容器启动后初始化定时任务
    public LockUtil() {
        // 创建定时任务线程池
        scheduledExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r, "LockUtil-ClearMap-Scheduler");
            thread.setDaemon(true);
            return thread;
        });
        // 启动定时清理任务
        scheduledExecutor.scheduleAtFixedRate(() -> {
            try {
                clearDoLockMap();
            } catch (Exception e) {
                log.error("定时清理 DO_LOCK_MAP 异常", e);
            }
        }, 5, 5, TimeUnit.MINUTES);
        log.info("LockUtil 定时清理任务已启动，每 5 分钟执行一次");
    }


    public static RedissonClient redissonClient;


    @Resource
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
            if (test) {
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
            // 表示为当前线程打中断标记：这里是代码扫描要求不能忽视中断异常
            Thread.currentThread().interrupt();
            // Thread.interrupted() 能告诉你线程是否发生中断，并将清除中断状态标记
            // 这里清除中断标记是为了让后续逻辑正常，中断表示我们没有获取到锁而已，至于谁通知我们中断的 暂时忽略
            Thread.interrupted();
        }
        return null;
    }


    public static void clearDoLockMap() {
        if (log.isDebugEnabled()) {
            log.debug("开始清理 DO_LOCK_MAP，当前大小：{}", DO_LOCK_MAP.size());
        }
        Iterator<Map.Entry<Long, FlowContext>> iterator = DO_LOCK_MAP.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, FlowContext> entry = iterator.next();
            if (entry == null) {
                continue;
            }
            FlowContext flowContext = entry.getValue();
            if (flowContext.isFlowEnd()) {
                iterator.remove();
                log.debug("移除已结束的流程：{}", entry.getKey());
                continue;
            }
            renewalWrapper(entry);
        }
    }

    private static void renewalWrapper(Map.Entry<Long, FlowContext> entry) {
        try {
            FlowContext value = entry.getValue();
            synchronized (value){
                if(value.isUnCasLockedPre()){
                    return;
                }
                renewalPo(value,value.getDataPo(),value.getMapper());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
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
        Method setLockStatus = aClass.getMethod("setLockStatus", Integer.class);
        int lock = mapper.lock(dataPo);
        if (lock > 0) {
            setLockVersion.invoke(dataPo, ((int) getLockVersion.invoke(dataPo)) + 1);
            setLockStatus.invoke(dataPo, YesOrNo.YES.ordinal());
            DO_LOCK_MAP.putIfAbsent(context.getUniqueValue(), context);
            context.setCasLocked(true);
            context.setMapper(mapper);
            context.setDataPo(dataPo);
            return true;
        }
        return false;
    }

    public static boolean unlockPo(FlowContext context) {
        DO_LOCK_MAP.remove(context.getUniqueValue());
        context.setUnCasLockedPre(true);
        Object dataPo = context.getDataPo();
        BaseMapper mapper = context.getMapper();

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
        int lock;
        synchronized (context){
            lock = mapper.unlock(dataPo);
        }
        boolean res = false;
        if (lock > 0) {
            try {
                setLockVersion.invoke(dataPo, ((long) getLockVersion.invoke(dataPo)) + 1L);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            res = true;
        }
        return res;
    }

    public static boolean renewalPo(FlowContext context,Object dataDo, BaseMapper mapper) throws Exception {
        Class<?> aClass = dataDo.getClass();
        Method setLockVersion = aClass.getMethod("setLockVersion", Long.class);
        Method getLockVersion = aClass.getMethod("getLockVersion");
        int lock = mapper.lock(dataDo);
        if (lock > 0) {
            context.setLockTime(Instant.now().getEpochSecond());
            setLockVersion.invoke(dataDo, ((long) getLockVersion.invoke(dataDo)) + 1L);
            return true;
        }
        return false;
    }

}
