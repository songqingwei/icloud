package cn.isqing.icloud.common.utils.validation;

import cn.isqing.icloud.common.utils.dto.BaseException;
import cn.isqing.icloud.common.utils.enums.ResCodeEnum;
import cn.isqing.icloud.common.utils.json.JsonUtil;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public class ValidationUtil {

    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();


    public static <T> void validateWithThrow(T object,Class<?>... groups) {
        Set<ConstraintViolation<T>> validateResult = VALIDATOR.validate(object);
        if (validateResult.isEmpty()) {
            return;
        }
        List<String> list =
                validateResult.stream().map(c -> c.getPropertyPath() + ":" + c.getMessage()).collect(Collectors.toList());
        throw new BaseException(ResCodeEnum.VALIDATE_ERROR.getCode(), JsonUtil.toJsonString(list));
    }

    public static <T> List<String> validate(T object,Class<?>... groups) {
        Set<ConstraintViolation<T>> validateResult = VALIDATOR.validate(object);
        if (validateResult.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        List<String> list =
                validateResult.stream().map(c -> c.getPropertyPath() + ":" + c.getMessage()).collect(Collectors.toList());
        return list;
    }

}