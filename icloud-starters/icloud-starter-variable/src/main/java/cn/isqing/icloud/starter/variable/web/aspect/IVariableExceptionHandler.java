package cn.isqing.icloud.starter.variable.web.aspect;

import cn.isqing.icloud.common.utils.dto.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Slf4j
@RestControllerAdvice("cn.isqing.icloud.starter.variable.web.controller")
public class IVariableExceptionHandler {

    @ExceptionHandler({Exception.class})
    public Response<Object> exceptionHandler(Exception e) {
        log.error(e.getMessage(), e);
        return Response.ERROR;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response<Object> validationBodyException(MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();
        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            errors.forEach(p -> {
                FieldError fieldError = (FieldError) p;
                log.error("Data check failure : object{},field{},errorMessage{}",
                        fieldError.getObjectName(),
                        fieldError.getField(),
                        fieldError.getDefaultMessage());

            });
            String msg = errors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining("\n"));
            return Response.error(msg);
        }
        return Response.ERROR;
    }
    // todo-sqw 测试是否生效 如会触发404的请求
}
