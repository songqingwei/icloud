package cn.isqing.icloud.common.utils.event;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 表操作事件消息
 *
 * @author songqingwei
 * @version 1.0
 */
@Data
public class TableOperationEventMsg implements Serializable {

    /**
     * 事件ID
     */
    private String eventId;

    /**
     * 事件类型（三级路由：TABLE_OP:INSERT:sys_user）
     */
    private String eventType;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 操作类型（INSERT/UPDATE/DELETE）
     */
    private String operation;

    /**
     * 数据ID
     */
    private Long dataId;

    /**
     * 操作数据（JSON字符串）
     */
    private String data;

    /**
     * 操作是否成功
     */
    private Boolean success;

    /**
     * 操作时间
     */
    private LocalDateTime operateTime;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 扩展信息
     */
    private String extInfo;

    public TableOperationEventMsg() {
        this.operateTime = LocalDateTime.now();
    }
}
