package cn.isqing.icloud.common.utils.event;

import cn.isqing.icloud.common.utils.constants.TableOperationEventConstants;
import cn.isqing.icloud.common.utils.event.factory.TableOperationEventSubscriberFactory;
import cn.isqing.icloud.common.utils.json.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * 表操作事件发布器
 * 根据事件类型（三级路由）自动找到对应的订阅者并发布事件
 *
 * @author songqingwei
 * @version 1.0
 */
@Component
@Slf4j
public class TableOperationEventPublisher {

    @Autowired
    private TableOperationEventSubscriberFactory factory;

    /**
     * 发布表操作事件
     *
     * @param eventMsg 事件消息
     */
    public void publishEvent(TableOperationEventMsg eventMsg) {
        try {
            // 生成事件ID
            if (eventMsg.getEventId() == null) {
                eventMsg.setEventId(UUID.randomUUID().toString().replace("-", ""));
            }

            // 构建事件类型（三级路由）
            String eventType = TableOperationEventConstants.buildEventType(
                    eventMsg.getOperation(), 
                    eventMsg.getTableName()
            );
            eventMsg.setEventType(eventType);

            log.info("发布表操作事件: eventType={}, dataId={}", eventType, eventMsg.getDataId());

            // 根据事件类型获取订阅者列表
            List<TableOperationEventSubscriber> subscribers = factory.get(
                    TableOperationEventConstants.R1_TABLE_OPERATION,
                    eventMsg.getOperation(),
                    eventMsg.getTableName()
            );

            if (subscribers == null || subscribers.isEmpty()) {
                log.debug("未找到事件订阅者: eventType={}", eventType);
                return;
            }

            // 并行执行所有订阅者
            subscribers.parallelStream().forEach(subscriber -> {
                try {
                    subscriber.onEvent(eventMsg);
                    log.debug("事件订阅者处理成功: subscriber={}", subscriber.getClass().getSimpleName());
                } catch (Exception e) {
                    log.error("事件订阅者处理失败: subscriber={}", subscriber.getClass().getSimpleName(), e);
                    // 不抛出异常，避免影响其他订阅者
                }
            });

            log.info("表操作事件发布完成: eventType={}, subscriberCount={}", 
                    eventType, subscribers.size());

        } catch (Exception e) {
            log.error("发布表操作事件失败", e);
        }
    }

    /**
     * 便捷方法：发布插入事件
     *
     * @param tableName 表名
     * @param dataId    数据ID
     * @param data      数据对象
     * @param success   是否成功
     */
    public void publishInsertEvent(String tableName, Long dataId, Object data, boolean success) {
        TableOperationEventMsg eventMsg = new TableOperationEventMsg();
        eventMsg.setTableName(tableName);
        eventMsg.setOperation(TableOperationEventConstants.R2_INSERT);
        eventMsg.setDataId(dataId);
        eventMsg.setData(data != null ? JsonUtil.toJsonString(data) : null);
        eventMsg.setSuccess(success);
        publishEvent(eventMsg);
    }

    /**
     * 便捷方法：发布更新事件
     *
     * @param tableName 表名
     * @param dataId    数据ID
     * @param data      数据对象
     * @param success   是否成功
     */
    public void publishUpdateEvent(String tableName, Long dataId, Object data, boolean success) {
        TableOperationEventMsg eventMsg = new TableOperationEventMsg();
        eventMsg.setTableName(tableName);
        eventMsg.setOperation(TableOperationEventConstants.R2_UPDATE);
        eventMsg.setDataId(dataId);
        eventMsg.setData(data != null ? JsonUtil.toJsonString(data) : null);
        eventMsg.setSuccess(success);
        publishEvent(eventMsg);
    }

    /**
     * 便捷方法：发布删除事件
     *
     * @param tableName 表名
     * @param dataId    数据ID
     * @param data      数据对象
     * @param success   是否成功
     */
    public void publishDeleteEvent(String tableName, Long dataId, Object data, boolean success) {
        TableOperationEventMsg eventMsg = new TableOperationEventMsg();
        eventMsg.setTableName(tableName);
        eventMsg.setOperation(TableOperationEventConstants.R2_DELETE);
        eventMsg.setDataId(dataId);
        eventMsg.setData(data != null ? JsonUtil.toJsonString(data) : null);
        eventMsg.setSuccess(success);
        publishEvent(eventMsg);
    }
}
