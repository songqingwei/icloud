package cn.isqing.icloud.starter.variable.web.aspect;


import cn.isqing.icloud.common.utils.dto.BaseException;
import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.common.api.enums.ResCodeEnum;
import cn.isqing.icloud.common.utils.log.MDCUtil;
import cn.isqing.icloud.common.utils.validation.ValidationUtil;
import cn.isqing.icloud.starter.variable.api.dto.AuthDto;
import cn.isqing.icloud.starter.variable.common.constants.CommonConfigGroupConstants;
import cn.isqing.icloud.starter.variable.dao.entity.CommonConfig;
import cn.isqing.icloud.starter.variable.dao.mapper.CommonConfigMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ControllerAspect {

    @Autowired
    private CommonConfigMapper configMapper;

    @Around("execution(* cn.isqing.icloud.starter.variable.web.controller..*.*(..))")
    public Object aroundMethod(ProceedingJoinPoint joinPoint) {
        MDCUtil.appendTraceId();
        try {
            Object[] args = joinPoint.getArgs();
            log.info("请求参数:{}", args);
            // 参数校验
            ValidationUtil.validateWithThrow(args);
            // 权限校验
            checkAuth(args);
            return joinPoint.proceed();
        } catch (BaseException e) {
            return Response.error(e.getCode(), e.getMessage());
        } catch (Throwable e) {
            return Response.ERROR;
        } finally {
            MDCUtil.cancelAppendTraceId();
        }
    }

    private void checkAuth(Object[] args) {
        CommonConfig config = new CommonConfig();
        config.setGroup(CommonConfigGroupConstants.DOMAIN_AUTH_CODE);
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg instanceof AuthDto) {
                AuthDto authDto = (AuthDto) arg;
                config.setKey(authDto.getDomain().toString());
                CommonConfig first = configMapper.first(config, null);
                if (first == null) {
                    throw new BaseException(ResCodeEnum.REJECT.getCode(), "不存在对应domain授权");
                }
                if (!first.getValue().equals(authDto.getDomainAuthCode())) {
                    throw new BaseException(ResCodeEnum.REJECT.getCode(), "domain授权校验不通过");
                }

            }
        }
    }

}
