package cn.isqing.icloud.common.utils.event;

import cn.isqing.icloud.common.utils.constants.TableOperationEventConstants;
import cn.isqing.icloud.common.utils.event.factory.EventSubscriberFactory;
import cn.isqing.icloud.common.utils.json.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * 事件发布器
 * 根据事件类型（三级路由）自动找到对应的订阅者并发布事件
 *
 * @author songqingwei
 * @version 1.0
 */
@Component
@Slf4j
public class EventPublisher {

    @Autowired
    private EventSubscriberFactory factory;

    /**
     * 发布事件
     *
     * @param eventMsg 事件消息
     */
    public void publishEvent(EventMsg eventMsg) {
        try {
            if (eventMsg.getEventId() == null) {
                eventMsg.setEventId(UUID.randomUUID().toString().replace("-", ""));
            }

            String eventType = TableOperationEventConstants.buildEventType(
                    eventMsg.getOperation(),
                    eventMsg.getTableName()
            );
            eventMsg.setEventType(eventType);

            log.info("发布事件: eventType={}, dataId={}", eventType, eventMsg.getDataId());

            List<EventSubscriber> subscribers = factory.get(
                    TableOperationEventConstants.R1_TABLE_OPERATION,
                    eventMsg.getOperation(),
                    eventMsg.getTableName()
            );

            if (subscribers == null || subscribers.isEmpty()) {
                log.debug("未找到事件订阅者: eventType={}", eventType);
                return;
            }

            subscribers.parallelStream().forEach(subscriber -> {
                try {
                    subscriber.onEvent(eventMsg);
                    log.debug("事件订阅者处理成功: subscriber={}", subscriber.getClass().getSimpleName());
                } catch (Exception e) {
                    log.error("事件订阅者处理失败: subscriber={}", subscriber.getClass().getSimpleName(), e);
                }
            });

            log.info("事件发布完成: eventType={}, subscriberCount={}", eventType, subscribers.size());

        } catch (Exception e) {
            log.error("发布事件失败", e);
        }
    }

    /**
     * 发布插入事件
     */
    public void publishInsertEvent(String tableName, Long dataId, Object data, boolean success) {
        EventMsg eventMsg = new EventMsg();
        eventMsg.setTableName(tableName);
        eventMsg.setOperation(TableOperationEventConstants.R2_INSERT);
        eventMsg.setDataId(dataId);
        eventMsg.setData(data != null ? JsonUtil.toJsonString(data) : null);
        eventMsg.setSuccess(success);
        publishEvent(eventMsg);
    }

    /**
     * 发布更新事件
     */
    public void publishUpdateEvent(String tableName, Long dataId, Object data, boolean success) {
        EventMsg eventMsg = new EventMsg();
        eventMsg.setTableName(tableName);
        eventMsg.setOperation(TableOperationEventConstants.R2_UPDATE);
        eventMsg.setDataId(dataId);
        eventMsg.setData(data != null ? JsonUtil.toJsonString(data) : null);
        eventMsg.setSuccess(success);
        publishEvent(eventMsg);
    }

    /**
     * 发布删除事件
     */
    public void publishDeleteEvent(String tableName, Long dataId, Object data, boolean success) {
        EventMsg eventMsg = new EventMsg();
        eventMsg.setTableName(tableName);
        eventMsg.setOperation(TableOperationEventConstants.R2_DELETE);
        eventMsg.setDataId(dataId);
        eventMsg.setData(data != null ? JsonUtil.toJsonString(data) : null);
        eventMsg.setSuccess(success);
        publishEvent(eventMsg);
    }
}
