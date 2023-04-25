package cn.isqing.icloud.starter.variable.service.event;

import cn.isqing.icloud.starter.variable.service.msg.dto.EventMsg;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public interface EventSubscriber {

    void onEvent(EventMsg eventMsg);
}
