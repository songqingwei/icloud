package cn.isqing.icloud.starter.drools.common.validation;

import cn.isqing.icloud.starter.drools.common.util.CronUtil;
import org.apache.commons.lang3.StringUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public class IsCronValidator implements ConstraintValidator<IsCron, String> {
    private boolean required = true;
    @Override
    public void initialize(IsCron constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(!required && value==null){
            return true;
        }
        if(StringUtils.isBlank(value)){
            return false;
        }
        return CronUtil.parse(value);
    }

}
