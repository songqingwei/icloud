package cn.isqing.icloud.starter.drools.service.event.event;

import cn.isqing.icloud.common.utils.constants.EventConstants;
import cn.isqing.icloud.starter.drools.service.event.EventSubscriber;
import cn.isqing.icloud.starter.drools.service.event.factory.EventSubscriberFactory;
import cn.isqing.icloud.starter.drools.service.msg.MsgParserService;
import cn.isqing.icloud.starter.drools.service.msg.dto.EventMsg;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = "${i.drools.event.cgroup:i-drools-event}",
        topic = "${i.drools.event.topic:i-drools-event}"
)
public class EventListener implements RocketMQListener<String> {

    @Autowired
    private EventSubscriberFactory factory;

    @Autowired
    private MsgParserService msgParserService;

    @Override
    public void onMessage(String message) {
        log.info("消费消息:{}", message);
        EventMsg eventMsg = msgParserService.parseEventMsg(message);
        List<EventSubscriber> list = factory.get(eventMsg.getEventType(), EventConstants.CLUSTERING_MODEL);
        list.parallelStream().forEach(s->{
            s.onEvent(eventMsg);
        });
    }


}
