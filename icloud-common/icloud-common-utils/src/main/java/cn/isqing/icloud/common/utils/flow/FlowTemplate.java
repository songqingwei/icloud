package cn.isqing.icloud.common.utils.flow;

import cn.isqing.icloud.common.utils.dto.Response;
import cn.isqing.icloud.common.utils.json.JsonUtil;
import cn.isqing.icloud.common.utils.log.MDCUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 流程模版
 *
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Slf4j
@SuppressWarnings("unchecked")
public class FlowTemplate<T extends FlowContext, R> {
    private String stepLog = "执行步骤:{}";
    private List<Step> steps = new ArrayList<>();
    private String flowName;
    private String flowClassName;

    private Consumer<T> errorAccept;
    private Function<T, Response<R>> errorApply;

    private Consumer<T> finallyAccept;
    private String finallyAcceptName;
    private Function<T, Response<R>> finallyApply;
    private String finallyApplyName;

    protected void start(String flowName, Object flow) {
        this.flowName = flowName;
        this.flowClassName = flow.getClass().getSimpleName();
    }

    @Data
    private class Step {
        private String name;
        private Predicate<T> test;
        private Consumer<T> accept;
        private Function<T, Response<R>> apply;
    }

    protected void errorAccept(Consumer<T> accept) {
        this.errorAccept = accept;
    }

    protected void errorApply(Function<T, Response<R>> apply) {
        this.errorApply = apply;
    }

    protected void finallyAccept(Consumer<T> accept) {
        this.finallyAccept = accept;
    }

    protected void finallyAcceptName(String name) {
        this.finallyAcceptName = name;
    }

    protected void finallyApply(Function<T, Response<R>> apply) {
        this.finallyApply = apply;
    }

    protected void finallyApplyName(String name) {
        this.finallyApplyName = name;
    }

    protected void stepName(String name) {
        Step step = new Step();
        step.setName(name);
        steps.add(step);
    }

    protected void test(Predicate<T> test) {
        Step step = steps.get(steps.size() - 1);
        step.test = test;
    }

    protected void accept(Consumer<T> accept) {
        Step step = steps.get(steps.size() - 1);
        step.accept = accept;
    }

    protected void apply(Function<T, Response<R>> apply) {
        Step step = steps.get(steps.size() - 1);
        step.apply = apply;
    }

    protected void interrupt(T context, Response<R> res) {
        context.setInterrupted(true);
        context.setFlowRes(res);
    }

    public final Response<R> exec(T context) {
        MDCUtil.appendTraceId();
        log.info("开始流程:{}[{}]", flowName, flowClassName);
        if (context.isLog()) {
            log.info("入参:{}", getString(context));
        }
        try {
            for (Step step : steps) {
                execStep(step, context);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            log.info("执行finally:");
            Optional.ofNullable(finallyAcceptName).ifPresent(name -> log.info(stepLog, name));
            Optional.ofNullable(finallyAccept).ifPresent(f -> f.accept(context));
            Optional.ofNullable(finallyApplyName).ifPresent(name -> log.info(stepLog, name));
            Optional.ofNullable(finallyApply).ifPresent(f -> context.setFlowRes(f.apply(context)));
            log.info("结束流程");
            if (context.isLog()) {
                log.info("最终上下文:{}", getString(context));
            }
            MDCUtil.cancelAppendTraceId();
        }
        if (context.getFlowErrorRes() != null) {
            return context.getFlowErrorRes();
        }
        if (context.getFlowRes() == null) {
            return (Response<R>) Response.SUCCESS;
        }
        return context.getFlowRes();
    }

    private void execStep(Step step, T context) {
        if (context.isInterrupted()) {
            log.info("isInterrupted=true跳过步骤:{}", step.getName());
            return;
        }
        if (step.getTest() != null && !step.getTest().test(context)) {
            log.info("跳过步骤:{}", step.getName());
            return;
        }
        log.info(stepLog, step.getName());
        try {
            Optional.ofNullable(step.getAccept()).ifPresent(f -> f.accept(context));
            Optional.ofNullable(step.getApply()).ifPresent(f -> context.setFlowRes(f.apply(context)));
        } catch (Exception e) {
            log.error("执行步骤[{}]异常:{}", step.getName(), e.getMessage());
            log.error(e.getMessage(), e);
            context.setInterrupted(true);
            Optional.ofNullable(errorAccept).ifPresent(f -> {
                log.info("执行errorAccept");
                f.accept(context);
            });
            Optional.ofNullable(errorApply).ifPresent(f -> {
                log.info("执行errorApply");
                context.setFlowErrorRes(f.apply(context));
            });
            if (context.getFlowErrorRes() == null) {
                context.setFlowEnd(true);
                throw e;
            }
        }
    }

    private String getString(T context) {
        return JsonUtil.toJsonString(context);
    }

}
