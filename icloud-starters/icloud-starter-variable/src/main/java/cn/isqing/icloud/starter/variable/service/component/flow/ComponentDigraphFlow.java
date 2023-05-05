package cn.isqing.icloud.starter.variable.service.component.flow;

import cn.isqing.icloud.common.utils.dto.Response;
import cn.isqing.icloud.common.utils.flow.FlowTemplate;
import cn.isqing.icloud.common.utils.kit.Digraph;
import cn.isqing.icloud.common.utils.kit.TopologicalSort;
import cn.isqing.icloud.starter.variable.common.constants.ComponentTextTypeConstants;
import cn.isqing.icloud.starter.variable.dao.entity.Component;
import cn.isqing.icloud.starter.variable.dao.entity.ComponentCondition;
import cn.isqing.icloud.starter.variable.dao.entity.ComponentText;
import cn.isqing.icloud.starter.variable.dao.entity.ComponentTextCondition;
import cn.isqing.icloud.starter.variable.dao.mapper.ComponentMapper;
import cn.isqing.icloud.starter.variable.dao.mapper.ComponentTextMapper;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Service
@Slf4j
public class ComponentDigraphFlow extends FlowTemplate<ComponentDigraphContext, List<List<Component>>> {

    @Value("${i.variable.loadDependency.selfCheckingLimit:1000}")
    private int selfCheckingLimit;
    @Value("${i.variable.loadDependency.selfCheckingSwitch:false}")
    private boolean selfCheckingSwitch;
    private int limit = 100;
    @Autowired
    private ComponentMapper componentMapper;
    @Autowired
    private ComponentTextMapper componentTextMapper;

    public ComponentDigraphFlow() {
        start("组件有向图流程", this);
        stepName("加载初始依赖");
        accept(this::loanBaseDependency);
        stepName("加载间接依赖");
        accept(this::loadIndirectDependency);
        stepName("组件拓扑排序");
        accept(this::topologicalSort);
    }

    private void topologicalSort(ComponentDigraphContext context) {
        Digraph<Long> digraph = new Digraph<>();
        digraph.setAdj(context.getDependencyMap());
        digraph.reverse();
        List<List<Long>> list = TopologicalSort.sort(digraph.getAdj());
        Map<Long, Component> map = context.getAllComponent();
        List<List<Component>> list1 = list.stream()
                .map(innerList -> innerList.stream().map(cid -> map.get(cid)).collect(Collectors.toList()))
                .collect(Collectors.toList());
        context.setFlowRes(Response.success(list1));
    }

    private void loadIndirectDependency(ComponentDigraphContext context) {
        Map<Long, Component> allComponent = context.getAllComponent();
        Map<Long, Set<Long>> dependencyMap = context.getDependencyMap();
        Set<Long> dependentCids = context.getDependentCids();
        Set<Long> excludeCid = context.getExcludeCidReq();
        dependentCids.removeAll(allComponent.keySet());
        if (excludeCid != null) {
            dependentCids.removeAll(excludeCid);
        }

        int num = 0;
        List<List<Long>> pageList;
        while (!dependentCids.isEmpty()) {
            num++;
            if (selfCheckingSwitch && num > selfCheckingLimit) {
                String msg = "依赖加载自检异常";
                log.error(msg + ":num:{}", num);
                interrupt(context, Response.error(msg));
                return;
            }
            // 分页拆分
            pageList = Stream.iterate(0, n -> n + limit)
                    .limit((dependentCids.size() / limit) + 1L)
                    .map(item -> dependentCids.stream().skip(item).limit(limit).collect(Collectors.toList()))
                    .collect(Collectors.toList());
            pageList.forEach(list1 -> {
                ComponentCondition condition = new ComponentCondition();
                condition.setIdCondition(list1);
                List<Component> components = componentMapper.selectByCondition(condition);
                components.forEach(c -> {
                    if (excludeCid != null && excludeCid.contains(c.getId())) {
                        return;
                    }
                    allComponent.put(c.getId(), c);
                });

                list1.removeAll(dependencyMap.keySet());
                if (!list1.isEmpty()) {
                    ComponentTextCondition condition1 = new ComponentTextCondition();
                    condition1.setFidCondition(list1);
                    condition1.setType(ComponentTextTypeConstants.DEPEND_CIDS);
                    List<ComponentText> componentTexts = componentTextMapper.selectByCondition(condition1);
                    Map<Long, String> collect1 =
                            componentTexts.stream().collect(Collectors.groupingBy(ComponentText::getFid,
                                    Collectors.mapping(ComponentText::getText, Collectors.joining())));

                    collect1.forEach((k, v) -> {
                        Set<Long> o = JSONObject.parseObject(v, new TypeReference<Set<Long>>() {
                        });
                        if (excludeCid != null && excludeCid.contains(k)) {
                            return;
                        }
                        if (excludeCid != null) {
                            o.removeAll(excludeCid);
                        }
                        dependencyMap.put(k, o);
                        dependentCids.addAll(o);
                    });
                }
            });
            dependentCids.removeAll(allComponent.keySet());
        }
    }

    private void loanBaseDependency(ComponentDigraphContext context) {
        Set<Long> excludeCid = context.getExcludeCidReq();
        List<Long> list = context.getCidReq();
        if (excludeCid != null) {
            list.removeAll(excludeCid);
        }

        Map<Long, Component> allComponent = context.getAllComponent();
        Map<Long, Set<Long>> dependencyMap = context.getDependencyMap();
        Set<Long> dependentCids = context.getDependentCids();
        List<List<Long>> pageList = Stream.iterate(0, n -> n + limit)
                .limit((list.size() / limit) + 1L)
                .map(item -> list.stream().skip(item).limit(limit).collect(Collectors.toList()))
                .collect(Collectors.toList());

        pageList.parallelStream().forEach(list1 -> {
            ComponentCondition condition = new ComponentCondition();
            condition.setIdCondition(list1);
            List<Component> components = componentMapper.selectByCondition(condition);
            components.forEach(c -> {
                if (excludeCid != null && excludeCid.contains(c.getId())) {
                    return;
                }
                allComponent.put(c.getId(), c);
                dependencyMap.put(c.getId(), Collections.emptySet());
            });

            ComponentTextCondition condition1 = new ComponentTextCondition();
            condition1.setFidCondition(list1);
            condition1.setType(ComponentTextTypeConstants.DEPEND_CIDS);
            List<ComponentText> componentTexts = componentTextMapper.selectByCondition(condition1);
            Map<Long, String> collect1 = componentTexts.stream().collect(Collectors.groupingBy(ComponentText::getFid,
                    Collectors.mapping(ComponentText::getText, Collectors.joining())));

            collect1.forEach((k, v) -> {
                Set<Long> o = JSONObject.parseObject(v, new TypeReference<Set<Long>>() {
                });
                if (excludeCid != null && excludeCid.contains(k)) {
                    return;
                }
                dependencyMap.put(k, o);
                dependentCids.addAll(o);
            });

        });
    }
}
