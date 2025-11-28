package cn.isqing.icloud.starter.drools.service.semaphore.util;

import cn.isqing.icloud.common.utils.constants.StrConstants;
import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.common.utils.json.JsonUtil;
import cn.isqing.icloud.starter.drools.service.semaphore.dto.AllotterConfigDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Slf4j
public class SingleRatioAllotter {

    private static final Map<String, BigDecimal> TOTAL_MAP = new ConcurrentHashMap<>();
    private static final Map<String, BigDecimal> S_MAP = new ConcurrentHashMap<>();
    private static final Map<String, BigDecimal> S_REF_MAP = new ConcurrentHashMap<>();

    // 跨日或手动清理
    public static void clear() {
        TOTAL_MAP.clear();
        S_MAP.clear();
        S_REF_MAP.clear();
    }


    /**
     * 获取目标ID
     *
     * @param coreId 核心ID
     * @param rid 规则ID
     * @param refValue 引用值
     * @param configDto 配置DTO
     * @return 目标ID响应
     */
    public static Response<Long> getTargetId(Long coreId, Long rid, String refValue, AllotterConfigDto configDto) {
        if (configDto.getTargetIds().isEmpty()) {
            log.warn("目标对象列表为空，无需计算分配目标");
            return Response.success(0L);
        }
        synchronized (configDto) {
            String totalKey = coreId + StrConstants.SEPARATOR + rid;
            BigDecimal total = TOTAL_MAP.getOrDefault(totalKey, BigDecimal.ZERO);
            Long targetId = null;
            if (total.longValue() < configDto.getTargetIds().size()) {
                // targetId = configDto.getTargetIds().get(configDto.getTargetIds().size() - 1 - total.intValue());
                List<Long> list = new ArrayList();
                list.addAll(configDto.getTargetIds());
                for (int i = 0; i < list.size(); i++) {
                    int index = (int)(Math.random() * list.size());
                    if(S_MAP.get(totalKey + StrConstants.SEPARATOR + list.get(index))==null){
                        targetId = list.get(index);
                        break;
                    }else {
                        list.remove(index);
                    }
                }
                S_MAP.put(totalKey + StrConstants.SEPARATOR + targetId, BigDecimal.ONE);
            } else {
                Map<Long, BigDecimal> completenessMap = new ConcurrentHashMap<>();
                CountDownLatch latch1 = new CountDownLatch(configDto.getTargetIds().size());
                // 计算当前比例及完成度
                configDto.getTargetIds().parallelStream().forEach(tid -> {
                    String key = totalKey + StrConstants.SEPARATOR + tid;
                    BigDecimal targetNum = S_MAP.getOrDefault(key, BigDecimal.ZERO);
                    BigDecimal targetRatio = targetNum.divide(total, 3, BigDecimal.ROUND_HALF_UP);
                    // 完成度
                    BigDecimal completeness = targetRatio.divide(configDto.getAllotMap().get(tid), 3,
                            BigDecimal.ROUND_HALF_UP);
                    completenessMap.put(tid, completeness);
                    latch1.countDown();
                });
                await(latch1);
                // 蛇形算法
                if (!StringUtils.isEmpty(refValue)) {
                    CountDownLatch latch2 = new CountDownLatch(configDto.getTargetIds().size());
                    // 计算当前比例及完成度
                    configDto.getTargetIds().parallelStream().forEach(tid -> {
                        String key = totalKey + StrConstants.SEPARATOR + tid;
                        BigDecimal targetNum = S_MAP.getOrDefault(key, BigDecimal.ZERO);
                        BigDecimal refTotal = S_REF_MAP.getOrDefault(key, BigDecimal.ZERO);
                        BigDecimal refAvg = refTotal.divide(targetNum, 3, BigDecimal.ROUND_HALF_UP);
                        // 综合完成度
                        completenessMap.put(tid, refAvg.multiply(completenessMap.get(key)));
                        latch2.countDown();
                    });
                    await(latch2);
                }
                // 求最小值
                targetId =
                        completenessMap.entrySet().stream().min(Comparator.comparing(Map.Entry::getValue)).get().getKey();
                log.info("完成度集合:{}", JsonUtil.toJsonString(completenessMap));
                String targetKey = totalKey + StrConstants.SEPARATOR + targetId;
                S_MAP.put(targetKey, S_MAP.get(targetKey).add(BigDecimal.ONE));
                if (!StringUtils.isEmpty(refValue)) {
                    // refTotal+1 是为了减小0.00？小数的误差防止按3位小数的精度四舍五入计算后都分给一个目标
                    S_REF_MAP.put(targetKey, S_REF_MAP.get(targetKey).add(new BigDecimal(refValue).add(BigDecimal.ONE)));
                }
            }
            log.info("目标解:{}", targetId);

            TOTAL_MAP.put(totalKey, total.add(BigDecimal.ONE));

            return Response.success(targetId);
        }
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
