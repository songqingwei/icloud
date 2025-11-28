package cn.isqing.icloud.starter.drools.service.semaphore.util;

import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.starter.drools.service.semaphore.dto.AllotterConfigDto;
import cn.isqing.icloud.starter.drools.service.semaphore.impl.FixedNumAllotter;
import cn.isqing.icloud.starter.drools.service.semaphore.impl.RatioAllotter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Slf4j
public class Allotter {

    @Getter
    private static final Map<Long, Map<Long, AllotterConfigDto>> CONFIG = new ConcurrentHashMap<>();


    public static void addConfig(Long coreId, Long rid, AllotterConfigDto configDto) {
        Map<Long, AllotterConfigDto> map = CONFIG.computeIfAbsent(coreId,
                coreId1 -> new ConcurrentHashMap<>());
        map.put(rid, configDto);
    }

    public static void romoveConfig(Long coreId) {
        CONFIG.remove(coreId);
    }

    /**
     * 获取目标ID
     *
     * @param coreId 核心ID
     * @param rid 规则ID
     * @param refValue 引用值
     * @return 目标ID响应
     */
    public static Response<Long> getTargetId(Long coreId, Long rid, String refValue) {
        Map<Long, AllotterConfigDto> map = CONFIG.get(coreId);
        String msg = "未获取到分配配置";
        if (map == null) {
            return Response.error(msg);
        }
        AllotterConfigDto dto = map.get(rid);
        if (dto == null) {
            return Response.error(msg);
        }
        switch (dto.getAllocationModel()) {
            case FIXED_NUM:
                return FixedNumAllotter.getAllotter().getTargetId(coreId, rid, refValue, dto);
            case SINGLE_RATIO:
                return SingleRatioAllotter.getTargetId(coreId, rid, refValue, dto);
            case RATIO:
                return RatioAllotter.getAllotter().getTargetId(coreId, rid, refValue, dto);
            default:
                break;
        }

        return null;
    }


}
