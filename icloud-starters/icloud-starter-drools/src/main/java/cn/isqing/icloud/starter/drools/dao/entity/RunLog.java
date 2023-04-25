package cn.isqing.icloud.starter.drools.dao.entity;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class RunLog {

    private Long id;

    // 限界id
    private Long coreId;

    private LocalDate busiDate;

    // 源id:外部业务标识
    private Long sourceId;

    private Long targetId;

    // 规则模版id
    private Long tid;

    private Integer lockStatus;

    private Long lockVersion;

    private LocalDateTime lockTime;

    private Integer status;

    private Integer subStatus;

    private Integer subFailNum;

    private String subFailReason;

    private Integer version;

    private Long actionId;

}
