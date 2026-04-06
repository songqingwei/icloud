package cn.isqing.icloud.common.utils.event;

/**
 * 事件订阅者接口
 * 通过 @RouteType 注解的三级路由自动注册到工厂
 *
 * @author songqingwei
 * @version 1.0
 */
public interface EventSubscriber {

    /**
     * 处理事件
     *
     * @param eventMsg 事件消息
     */
    void onEvent(EventMsg eventMsg);
}
