package cn.isqing.icloud.common.utils.annotation;

import java.lang.annotation.*;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RouteType {

    String r1() default "";

    String r2() default "";

    String r3() default "";
}
