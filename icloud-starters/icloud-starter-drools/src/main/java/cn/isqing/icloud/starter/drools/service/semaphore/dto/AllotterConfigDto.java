package cn.isqing.icloud.starter.drools.service.semaphore.dto;

import cn.isqing.icloud.starter.drools.common.enums.AlgorithModel;
import cn.isqing.icloud.starter.drools.common.enums.AllocationModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Data
public class AllotterConfigDto {

    private Long rid;
    private Long coreId;

    // 目标群
    private List<Long> targetIds;

    private Map<Long,String> targetNames;

    // 分配方案：比例或个数
    private Map<Long, BigDecimal> allotMap;

    // 算法模型
    private AlgorithModel  algorithModel;

    // 分配模型
    private AllocationModel allocationModel;

}
