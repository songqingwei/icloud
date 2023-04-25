package cn.isqing.icloud.starter.drools.service.component.flow;

import cn.hutool.core.collection.ConcurrentHashSet;
import cn.isqing.icloud.common.utils.flow.FlowContext;
import cn.isqing.icloud.starter.drools.dao.entity.Component;
import lombok.Data;

import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Data
public class ComponentDigraphContext extends FlowContext<Deque<Component>> {
    // 初始基础依赖组件id
    private List<Long> cidReq;
    // 排除组件id
    private Set<Long> excludeCidReq;

    // 所需要的所有组件map
    Map<Long, Component> allComponent = new ConcurrentHashMap<>();
    // 依赖关系图
    Map<Long, Set<Long>> dependencyMap = new ConcurrentHashMap<>();
    // 待查询依赖项
    Set<Long> dependentCids = new ConcurrentHashSet<>();
}
