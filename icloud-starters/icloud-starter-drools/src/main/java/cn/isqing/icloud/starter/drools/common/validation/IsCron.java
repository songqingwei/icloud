package cn.isqing.icloud.starter.drools.common.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {IsCronValidator.class}) // 标明由哪个类执行校验逻辑
public @interface IsCron {

    String message() default "cron表达式错误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    //自定义属性-是否需要：不需要即可以为空
    boolean required() default true;

}
