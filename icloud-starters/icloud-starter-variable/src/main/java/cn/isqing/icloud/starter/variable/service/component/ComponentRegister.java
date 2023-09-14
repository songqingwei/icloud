package cn.isqing.icloud.starter.variable.service.component;

import java.util.function.UnaryOperator;

/**
 * 组件注册器
 *
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public interface ComponentRegister {

    /**
     * @param name     注册名称
     * @param function 入参即解析后的配置字符串，返参要求是json字符串
     */
    void register(String name, UnaryOperator<String> function);

    /**
     * @param name     注册名称
     * @param function 入参即解析后的配置字符串，返参要求是json字符串
     */
    UnaryOperator<String> get(String name);

}
