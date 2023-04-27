package cn.isqing.icloud.starter.variable.service.event;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public interface EventPublisher {

    void publishEvent(String id,String eventType,Object... datas);
    void publishBcEvent(String id,String eventType,Object... datas);

}
