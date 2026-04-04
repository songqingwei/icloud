package cn.isqing.icloud.common.utils.event;

/**
 * 表操作事件订阅者接口
 * 通过@RouteType注解的三级路由自动注册到工厂
 *
 * @author songqingwei
 * @version 1.0
 */
public interface TableOperationEventSubscriber {

    /**
     * 处理表操作事件
     *
     * @param eventMsg 事件消息
     */
    void onEvent(TableOperationEventMsg eventMsg);
}
