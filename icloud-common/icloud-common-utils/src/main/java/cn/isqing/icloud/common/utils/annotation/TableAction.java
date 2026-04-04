package cn.isqing.icloud.common.utils.annotation;

import cn.isqing.icloud.common.utils.enums.ActionType;

import java.lang.annotation.*;

/**
 * 控制器方法注解，用于定义表操作相关信息
 * 包含表名、操作类型和VO类
 *
 * @author songqingwei
 * @version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TableAction {
    
    /**
     * 数据库表名
     * 
     * @return 表名
     */
    String tableName() default "";
    
    /**
     * 操作类型
     * 
     * @return 操作类型枚举值
     */
    ActionType action() default ActionType.LIST_QUERY;
    
    /**
     * VO类
     * 
     * @return VO类的Class对象
     */
    Class<?> voClass() default Object.class;
}
