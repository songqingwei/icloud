package cn.isqing.icloud.common.utils.event.factory;

import cn.isqing.icloud.common.utils.bean.BaseFactory;
import cn.isqing.icloud.common.utils.event.EventSubscriber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 事件订阅者工厂
 * 通过三级路由自动找到对应的订阅者实现类
 *
 * @author songqingwei
 * @version 1.0
 */
@Service
@Slf4j
public class EventSubscriberFactory extends BaseFactory<EventSubscriber> {

}
