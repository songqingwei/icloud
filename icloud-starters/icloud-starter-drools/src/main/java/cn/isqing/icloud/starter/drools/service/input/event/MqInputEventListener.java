package cn.isqing.icloud.starter.drools.service.input.event;

import cn.isqing.icloud.starter.drools.service.input.dto.InputDto;
import cn.isqing.icloud.starter.drools.service.input.flow.InputFlow;
import cn.isqing.icloud.starter.drools.service.input.flow.InputFlowContext;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Slf4j
@RocketMQMessageListener(
        consumerGroup = "${i.drools.mqInput:i-drools-mqInput}",
        topic = "${i.drools.mqInput.topic:i-drools-mqInput}",
        messageModel = MessageModel.BROADCASTING
)
public class MqInputEventListener implements RocketMQListener<String> {

    @Autowired
    private InputFlow flow;

    @Override
    public void onMessage(String message) {
        log.info("事件:mqInput,消费消息:{}", message);
        InputFlowContext context = new InputFlowContext();
        context.setInputDto(JSON.parseObject(message, InputDto.class));
        flow.exec(context);
    }


}
