package cn.isqing.icloud.starter.variable.service.event.impl;

import cn.isqing.icloud.common.utils.kit.MqUtil;
import cn.isqing.icloud.starter.variable.service.event.EventPublisher;
import cn.isqing.icloud.starter.variable.service.msg.MsgParserService;
import cn.isqing.icloud.starter.variable.service.msg.dto.EventMsg;
import com.alibaba.fastjson2.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Service
public class EventPublisherImpl implements EventPublisher {
    @Value("${i.variable.event.topic:i-variable-event}")
    private String topic;

    @Value("${i.variable.bc-event.topic:i-variable-bc-event}")
    private String bcTopic;

    @Autowired
    private MsgParserService msgParserService;

    @Override
    public void publishEvent(String id, String eventType,Object... datas) {
        EventMsg eventMsg = getEventMsg(id, eventType, datas);
        MqUtil.asyncSend(topic, msgParserService.assembleMsg(eventMsg));
    }

    private static EventMsg getEventMsg(String id, String eventType, Object[] datas) {
        EventMsg eventMsg = new EventMsg();
        eventMsg.setId(id);
        eventMsg.setEventType(eventType);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < datas.length; i++) {
            Object data = datas[i];
            list.add(JSON.toJSONString(data));
        }
        eventMsg.setData(list);
        return eventMsg;
    }

    @Override
    public void publishBcEvent(String id, String eventType, Object... datas) {
        EventMsg eventMsg = getEventMsg(id, eventType, datas);
        MqUtil.asyncSend(bcTopic, msgParserService.assembleMsg(eventMsg));
    }

}
