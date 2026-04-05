package cn.isqing.icloud.common.utils.kit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 消息队列工具类
 * 支持 RocketMQ，如果未配置则自动禁用
 *
 * @author songqingwei@aliyun.com
 * @version 1.0
 */
@Component
@Slf4j
public class MqUtil {

    private static Object mqTemplate;

    /**
     * 检查 MQ 是否可用
     */
    private static boolean isMqAvailable() {
        return mqTemplate != null;
    }

    // Spring 容器启动后初始化
    public MqUtil(ApplicationContext applicationContext) {
        // 尝试从容器中获取 RocketMQTemplate
        try {
            Object template = applicationContext.getBean("rocketMQTemplate");
            if (template != null) {
                MqUtil.mqTemplate = template;
                log.info("RocketMQTemplate 已初始化，MQ 功能已启用");
            }
        } catch (Exception e) {
            log.warn("RocketMQTemplate 未配置，MQ 功能已禁用");
        }
    }

    public static void asyncSend(String destination, Object msg) {
        if (!isMqAvailable()) {
            log.debug("MQ 未配置，跳过消息发送: {}", destination);
            return;
        }
        
        try {
            // 使用反射调用 asyncSend 方法
            Class<?> templateClass = mqTemplate.getClass();
            java.lang.reflect.Method asyncSendMethod = templateClass.getMethod("asyncSend", String.class, Object.class, 
                Class.forName("org.apache.rocketmq.client.producer.SendCallback"));
            
            // 创建 SendCallback 实例（使用反射）
            Class<?> sendCallbackClass = Class.forName("org.apache.rocketmq.client.producer.SendCallback");
            Object callback = java.lang.reflect.Proxy.newProxyInstance(
                sendCallbackClass.getClassLoader(),
                new Class[]{sendCallbackClass},
                (proxy, method, args) -> {
                    if ("onSuccess".equals(method.getName())) {
                        log.info("发送消息成功:destination:{},msg:{}", destination, msg);
                    } else if ("onException".equals(method.getName())) {
                        log.error("发送消息异常:destination:{},msg:{}", destination, msg, args[0]);
                    }
                    return null;
                }
            );
            
            asyncSendMethod.invoke(mqTemplate, destination, msg, callback);
        } catch (Exception e) {
            log.error("发送消息异常: {}", e.getMessage(), e);
        }
    }

}
