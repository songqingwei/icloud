package cn.isqing.icloud.starter.drools.service.event;

import cn.isqing.icloud.starter.drools.service.msg.dto.EventMsg;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public interface EventSubscriber {

    void onEvent(EventMsg eventMsg);
}
