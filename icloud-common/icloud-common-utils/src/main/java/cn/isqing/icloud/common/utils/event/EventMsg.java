package cn.isqing.icloud.common.utils.event;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 事件消息
 *
 * @author songqingwei
 * @version 1.0
 */
@Data
public class EventMsg implements Serializable {

    private String eventId;

    /** 事件类型（三级路由：TABLE_OP:INSERT:sys_user） */
    private String eventType;

    private String tableName;

    /** 操作类型（INSERT/UPDATE/DELETE） */
    private String operation;

    private Long dataId;

    /** 操作数据（JSON字符串） */
    private String data;

    private Boolean success;

    private LocalDateTime operateTime;

    private String operator;

    private String extInfo;

    public EventMsg() {
        this.operateTime = LocalDateTime.now();
    }
}
