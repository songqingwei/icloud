package cn.isqing.icloud.starter.variable.service.component.impl;

import cn.isqing.icloud.starter.variable.service.component.ComponentRegister;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

@Service
public class ComponentRegisterImpl implements ComponentRegister {

    private Map<String, UnaryOperator<String>> functions = new HashMap<>();

    /**
     * @param name     注册名称
     * @param function 入参即解析后的配置字符串，返参要求是json字符串
     */
    @Override
    public void register(String name, UnaryOperator<String> function) {
        functions.put(name, function);
    }

    /**
     * @param name 注册名称
     */
    @Override
    public UnaryOperator<String> get(String name) {
        return functions.get(name);
    }
}
