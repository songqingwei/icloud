package cn.isqing.icloud.common.utils.kit;

import cn.isqing.icloud.common.utils.dao.BaseMapper;
import cn.isqing.icloud.common.utils.enums.status.YesOrNo;
import cn.isqing.icloud.common.utils.flow.FlowContext;
import cn.isqing.icloud.common.utils.time.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * 分布式锁工具类
 * 支持 Redisson 分布式锁和数据库乐观锁
 * 如果 Redisson 不可用，自动降级为仅使用数据库锁
 *
 * @author songqingwei@aliyun.com
 * @version 1.0
 */
@Component
@Slf4j
public class LockUtil {

    private static final Map<Long, FlowContext> DO_LOCK_MAP = new ConcurrentHashMap<>();

    private static ScheduledExecutorService scheduledExecutor;

    // Spring 容器启动后初始化定时任务
    public LockUtil(ApplicationContext applicationContext) {
        // 尝试从容器中获取 RedissonClient
        try {
            Object redissonClient = applicationContext.getBean("redissonClient");
            if (redissonClient != null) {
                LockUtil.redissonClient = redissonClient;
                log.info("RedissonClient 已初始化，分布式锁功能已启用");
            }
        } catch (Exception e) {
            log.warn("RedissonClient 未配置，将仅使用数据库乐观锁");
        }
        
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


    public static Object redissonClient;

    /**
     * 检查 Redisson 是否可用
     */
    private static boolean isRedissonAvailable() {
        return redissonClient != null;
    }

    public static Object getRedisLock(String key) {
        return getRedisLock(key, 5, TimeUnit.SECONDS);
    }


    public static void tryRunWithRLock(String key, long time, TimeUnit unit, Predicate predicate, Consumer consumer) {
        Object rLock = getRedisLock(key, time, unit);
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
                // 使用反射调用 unlock 方法
                Method unlockMethod = rLock.getClass().getMethod("unlock");
                unlockMethod.invoke(rLock);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    public static Object getRedisLock(String key, long time, TimeUnit unit) {
        if (!isRedissonAvailable()) {
            log.debug("Redisson 未配置，跳过分布式锁: {}", key);
            return null;
        }
        try {
            // 使用反射调用 getLock 和 tryLock 方法
            Method getLockMethod = redissonClient.getClass().getMethod("getLock", String.class);
            Object lock = getLockMethod.invoke(redissonClient, key);
            
            Method tryLockMethod = lock.getClass().getMethod("tryLock", long.class, TimeUnit.class);
            boolean locked = (Boolean) tryLockMethod.invoke(lock, time, unit);
            
            if (locked) {
                log.debug("成功获取分布式锁: {}", key);
                return lock;
            } else {
                log.debug("获取分布式锁失败: {}", key);
            }
        } catch (Exception e) {
            log.error("获取锁失败: {}", key, e);
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
