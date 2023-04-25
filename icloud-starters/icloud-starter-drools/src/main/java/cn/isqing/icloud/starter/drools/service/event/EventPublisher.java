package cn.isqing.icloud.starter.drools.service.event;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public interface EventPublisher {

    /**
     * 集群事件
     *
     * @param id
     * @param eventType
     * @param datas
     */
    void publishEvent(Long id, String eventType, Object... datas);

    /**
     * 广播事件
     *
     * @param id
     * @param eventType
     * @param datas
     */
    void publishBcEvent(Long id, String eventType, Object... datas);

}
