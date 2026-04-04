package cn.isqing.icloud.common.utils.aspect;

import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.common.utils.annotation.TableAction;
import cn.isqing.icloud.common.utils.dto.TableOperationDto;
import cn.isqing.icloud.common.utils.json.JsonUtil;
import cn.isqing.icloud.common.utils.service.TableOperationService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 表操作注解切面
 * 处理@TableAction注解，自动执行对应的表操作
 *
 * @author songqingwei
 * @version 1.0
 */
@Aspect
@Component
@Order(100)
@Slf4j
public class TableOperationAspect {

    @Autowired
    private TableOperationService tableOperationService;

    /**
     * 拦截带有@TableAction注解的Controller方法
     *
     * @param joinPoint 连接点
     * @return 方法执行结果
     * @throws Throwable 异常
     */
    @Around("@annotation(cn.isqing.icloud.common.utils.annotation.TableAction)")
    public Object handleTableAction(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        
        TableAction tableAction = method.getAnnotation(TableAction.class);
        
        // 如果存在@TableAction注解，则使用通用表操作服务处理
        if (tableAction != null) {
            log.info("处理@TableAction注解: tableName={}, action={}", 
                tableAction.tableName(), tableAction.action());
            
            // 获取方法参数
            Object[] args = joinPoint.getArgs();
            
            // 创建TableOperationDto
            TableOperationDto tableOperationDto = new TableOperationDto();
            tableOperationDto.setTableName(tableAction.tableName());
            tableOperationDto.setAction(tableAction.action());
            tableOperationDto.setResVoClass(tableAction.voClass());
            
            // 将参数转换为JSON字符串
            if (args.length > 0) {
                String reqJson;
                // 如果只有一个参数且是String类型，直接使用
                if (args.length == 1 && args[0] instanceof String) {
                    reqJson = (String) args[0];
                } else {
                    Response<String> jsonResponse = JsonUtil.toJsonRes(args);
                    if (!jsonResponse.isSuccess()) {
                        log.error("参数转换为JSON失败: {}", jsonResponse.getMsg());
                        return Response.error("参数转换失败: " + jsonResponse.getMsg());
                    }
                    reqJson = jsonResponse.getData();
                }
                tableOperationDto.setReq(reqJson);
            }
            
            // 执行表操作
            Response<Object> response = tableOperationService.execute(tableOperationDto);
            log.info("表操作执行完成: tableName={}, action={}, result={}", 
                tableAction.tableName(), tableAction.action(), response.isSuccess());
            return response;
        }
        
        // 如果没有注解，继续执行原方法
        return joinPoint.proceed();
    }
}
