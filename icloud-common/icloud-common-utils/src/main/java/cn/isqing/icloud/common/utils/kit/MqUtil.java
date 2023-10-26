package cn.isqing.icloud.common.utils.kit;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Component
@Slf4j
public class MqUtil {

    private static RocketMQTemplate mqTemplate;

    @Autowired(required = false)
    public void setMqTemplate(RocketMQTemplate mqTemplate) {
        MqUtil.mqTemplate = mqTemplate;
    }

    public static void asyncSend(String destination, Object msg) {
        try {
            mqTemplate.asyncSend(destination, msg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    if (!sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                        log.info("发送消息失败:destination:{},msg:{},sendStatus:{}", destination, msg, sendResult.getSendStatus());
                    } else {
                        log.info("发送消息成功:destinationc:{},msg:{}", destination, msg);
                    }
                }

                @Override
                public void onException(Throwable throwable) {
                    log.info("发送消息异常:destination:{},msg:{}", destination, msg);
                    log.error("发送消息异常:" + throwable.getMessage(), throwable);
                }
            });
        } catch (Exception e) {
            log.error("发送消息异常："+e.getMessage(),e);
        }
    }

    public static void syncSend(String destination, Object msg) {
        try {
            SendResult sendResult = mqTemplate.syncSend(destination, msg);
            if (sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                log.info("发送消息成功: destination: {}, msg: {}", destination, msg);
            } else {
                log.info("发送消息失败: destination: {}, msg: {}, sendStatus: {}", destination, msg, sendResult.getSendStatus());
            }
        } catch (Exception e) {
            log.error("发送消息异常：" + e.getMessage(), e);
        }
    }

}
