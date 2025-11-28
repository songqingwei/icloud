package cn.isqing.icloud.starter.drools.service.event;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public interface EventPublisher {

    /**
     * 集群事件
     *
     * @param id 事件ID
     * @param eventType 事件类型
     * @param datas 事件数据
     */
    void publishEvent(Long id, String eventType, Object... datas);

    /**
     * 广播事件
     *
     * @param id 事件ID
     * @param eventType 事件类型
     * @param datas 事件数据
     */
    void publishBcEvent(Long id, String eventType, Object... datas);

}
