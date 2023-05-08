package cn.isqing.icloud.starter.variable.common.constants;


import cn.isqing.icloud.starter.variable.common.enums.DubboComponentDialectType;
import cn.isqing.icloud.starter.variable.common.enums.SqlComponentDialectType;

/**
 * 值不要重复
 * 不用枚举是防止太多了扩展不变
 * 太多了的则可以新建多个常量类
 * Map<placeholder,jsonPath>
 * placeholder：我方占位符
 * jsonPath：获取值的路径表达式
 *
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public class ComponentTextTypeConstants {
    private ComponentTextTypeConstants() {
    }

    /**
     * {@link DubboComponentDialectType}
     * {@link SqlComponentDialectType}
     */
    public static final int DIALECT_CONFIG = 1;


    // Map<placeholder,jsonPath>
    public static final int DEPEND_INPUT_PARAMS = 2;

    // Map<placeholder,jsonPath>
    public static final int DEPEND_C_RES = 3;

    // 依赖组件id set<Long>
    public static final int DEPEND_CIDS = 4;

    // 依赖的常量参数 MAP<placeholder,常量map的key>
    public static final int DEPEND_CONSTANTS = 5;

    // 系统变量参数 MAP<placeholder,系统变量map的key>
    public static final int DEPEND_SYSTEM_VARS = 6;

    // 自身的常量 MAP<placeholder,value>
    public static final int SELF_CONSTANTS = 7;


    // 判断结果的 [sucessValue jsonPath]
    public static final int RES_JUDGE = 111;

}
