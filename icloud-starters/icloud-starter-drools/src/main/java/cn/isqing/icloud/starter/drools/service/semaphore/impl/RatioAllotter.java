package cn.isqing.icloud.starter.drools.service.semaphore.impl;

import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.common.utils.json.JsonUtil;
import cn.isqing.icloud.common.utils.kit.LockUtil;
import cn.isqing.icloud.common.utils.kit.RedisUtil;
import cn.isqing.icloud.common.utils.kit.StrUtil;
import cn.isqing.icloud.common.utils.time.TimeUtil;
import cn.isqing.icloud.starter.drools.common.constants.LockScenarioConstants;
import cn.isqing.icloud.starter.drools.dao.entity.RatioAllotterLog;
import cn.isqing.icloud.starter.drools.dao.mapper.RatioAllotterLogMapper;
import cn.isqing.icloud.starter.drools.service.semaphore.dto.AllotterConfigDto;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Slf4j
@Service
public class RatioAllotter {

    @Value("${i.drools.highPrecisionRatioAllotter.tryLockTimeLimit:600}")
    private long tryLockTimeLimit;

    @Autowired
    private RatioAllotterLogMapper mapper;
    @Getter
    private static RatioAllotter allotter;

    @PostConstruct
    private void init() {
        allotter = this;
    }


    private Response<Long> getTargetIdFlow(Long coreId, Long rid, String refValue, AllotterConfigDto configDto) {
        if (configDto.getTargetIds().isEmpty()) {
            log.warn("目标对象列表为空，无需计算分配目标");
            return Response.success(0L);
        }

        Long targetId = null;
        // 获取total
        RatioAllotterLog totalRecord = getDbTotalRecord(coreId, rid);
        if (totalRecord.getNum().longValue() < configDto.getTargetIds().size()) {
            List<Long> list = new ArrayList();
            list.addAll(configDto.getTargetIds());
            for (int i = 0; i < list.size(); i++) {
                int index = (int) (Math.random() * list.size());
                Long tid = list.get(index);
                RatioAllotterLog record = getDbRecord(coreId, rid, tid);
                if (record.getNum().equals(0L)) {
                    targetId = list.get(index);
                    break;
                } else {
                    list.remove(index);
                }
            }
        } else {
            Map<Long, BigDecimal> completenessMap = new ConcurrentHashMap<>();
            Map<Long, RatioAllotterLog> nowMap = new HashMap<>();
            CountDownLatch latch1 = new CountDownLatch(configDto.getTargetIds().size());
            // 计算当前比例及完成度
            configDto.getTargetIds().parallelStream().forEach(tid -> {
                RatioAllotterLog record = getDbRecord(coreId, rid, tid);
                nowMap.put(tid, record);
                if (record.getNum().equals(0L)) {
                    completenessMap.put(tid, BigDecimal.ZERO);
                    latch1.countDown();
                    return;
                }
                BigDecimal completeness = new BigDecimal(record.getNum().toString()).divide(configDto.getAllotMap().get(tid), 3, BigDecimal.ROUND_HALF_UP);
                completenessMap.put(tid, completeness);
                latch1.countDown();
            });
            await(latch1);
            // 蛇形算法
            if (!StringUtils.isEmpty(refValue)) {
                CountDownLatch latch2 = new CountDownLatch(configDto.getTargetIds().size());
                // 计算当前比例及完成度
                nowMap.entrySet().parallelStream().forEach(e -> {
                    Long num = e.getValue().getNum();
                    if (num <= 0) {
                        latch2.countDown();
                        return;
                    }
                    BigDecimal refAvg = e.getValue().getRefTotal().divide(new BigDecimal(num.toString()), 3, BigDecimal.ROUND_HALF_UP);
                    // 综合完成度
                    completenessMap.put(e.getKey(), refAvg.multiply(completenessMap.get(e.getKey())));
                    latch2.countDown();
                });
                await(latch2);
            }

            // 求最小值
            targetId =
                    completenessMap.entrySet().stream().min(Comparator.comparing(Map.Entry::getValue)).get().getKey();
            log.info("完成度集合:{}", JsonUtil.toJsonString(completenessMap));
            incrDbRecord(coreId, rid, targetId, refValue);
            incrDbTotalRecord(coreId, rid);
        }

        log.info("目标解:{}", targetId);
        return Response.success(targetId);

    }

    private void incrDbRecord(Long coreId, Long rid, Long tid, String refValue) {
        LocalDate busiDate = TimeUtil.now().toLocalDate();
        String dateStr = busiDate.format(TimeUtil.simpleDateFormatter);
        String uid = StrUtil.assembleKey(dateStr, coreId.toString(), rid.toString(), tid.toString());
        BigDecimal refAdd = BigDecimal.ZERO;
        if(!StringUtils.isEmpty(refValue)){
            // ref +1 是为了减小0.00？小数的误差防止按3位小数的精度四舍五入计算后都分给一个目标
            refAdd = new BigDecimal(refValue).add(BigDecimal.ONE);
        }
        mapper.incrByUid(uid,refAdd);
    }

    private void incrDbTotalRecord(Long coreId, Long rid) {
        LocalDate busiDate = TimeUtil.now().toLocalDate();
        String dateStr = busiDate.format(TimeUtil.simpleDateFormatter);
        String uid = StrUtil.assembleKey(dateStr, coreId.toString(), rid.toString());
        mapper.incrNumByUid(uid);
    }


    public Response<Long> getTargetId(Long coreId, Long rid, String refValue, AllotterConfigDto configDto) {
        String key = RedisUtil.getKey(LockScenarioConstants.RATIO_ALLOTTER, coreId.toString(), rid.toString());
        RLock rLock = LockUtil.getRedisLock(key, tryLockTimeLimit, TimeUnit.SECONDS);
        if (rLock == null) {
            return Response.error("获取redis锁失败");
        }
        try {
            return getTargetIdFlow(coreId, rid, refValue, configDto);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.error("计算目标值异常");
        } finally {
            try {
                rLock.unlock();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private RatioAllotterLog getDbRecord(Long coreId, Long rid, Long tid) {
        LocalDate busiDate = TimeUtil.now().toLocalDate();
        String dateStr = busiDate.format(TimeUtil.simpleDateFormatter);
        String uid = StrUtil.assembleKey(dateStr, coreId.toString(), rid.toString(), tid.toString());
        RatioAllotterLog data = new RatioAllotterLog();
        data.setUid(uid);
        RatioAllotterLog first = mapper.first(data, null);
        if (first != null) {
            return first;
        }
        data.setBusiDate(busiDate);
        data.setCoreId(coreId);
        data.setRid(rid);
        data.setTargetId(tid);
        data.setRefTotal(BigDecimal.ZERO);
        data.setNum(0L);
        mapper.insert(data);
        return data;
    }

    private RatioAllotterLog getDbTotalRecord(Long coreId, Long rid) {
        LocalDate busiDate = TimeUtil.now().toLocalDate();
        String dateStr = busiDate.format(TimeUtil.simpleDateFormatter);
        String uid = StrUtil.assembleKey(dateStr, coreId.toString(), rid.toString());
        RatioAllotterLog data = new RatioAllotterLog();
        data.setUid(uid);
        RatioAllotterLog first = mapper.first(data, null);
        if (first != null) {
            return first;
        }
        data.setBusiDate(busiDate);
        data.setCoreId(coreId);
        data.setRid(rid);
        data.setRefTotal(BigDecimal.ZERO);
        data.setNum(0L);
        mapper.insert(data);
        return data;
    }

    private static void await(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            // 表示为当前线程打中断标记:这里是代码扫描要求不能忽视中断异常
            Thread.currentThread().interrupt();
            // Thread.interrupted()能告诉你线程是否发生中断,并将清除中断状态标记
            // 这里清除中断标记是为了让后续逻辑正常，中断表示我们没有获取到锁而已，至于谁通知我们中断的 暂时忽略
            Thread.interrupted();
        }
    }

}
