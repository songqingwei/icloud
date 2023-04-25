package cn.isqing.icloud.starter.variable.common.constants;


import cn.isqing.icloud.starter.variable.common.enums.DubboComponentDialectType;
import cn.isqing.icloud.starter.variable.common.enums.SqlComponentDialectType;

/**
 * 值不要重复
 * 不用枚举是防止太多了扩展不变
 * 太多了的则可以新建多个常量类
 *
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public class ComponentTextTypeConstants {
    private ComponentTextTypeConstants() {
    }


    // 公共-----------------------------------------
    // set<name>
    public static final int DEPEND_INPUT_PARAMS = 1;
    // set<res_name>
    public static final int DEPEND_C_RES_NAME = 2;
    // 依赖组件id-json[set<Long>]
    public static final int DEPEND_CID = 3;
    /**
     * {@link DubboComponentDialectType}
     * {@link SqlComponentDialectType}
     */
    public static final int DIALECT_CONFIG = 4;

    /**
     * 常量参数 MAP<jsonPath/sql placeholder,key>
     * jsonPath
     * key 用来定位常量
     */
    public static final int CONSTANT_PARAMS = 5;

    /**
     * 系统变量参数 MAP<sonPath/sql placeholder,key>
     * jsonPath
     * key 用来定位常量
     */
    public static final int VARIABLE_PARAMS = 6;


    /**
     * 输入参数 MAP<sonPath/sql placeholder,key>
     * jsonPath
     * key 用来定位常量
     */
    public static final int INPUT_PARAMS = 7;

    /**
     * 上文结果集参数 MAP<sonPath/sql placeholder,key>
     * jsonPath
     * key 用来定位常量
     */
    public static final int ABOVE_RES_PARAMS = 8;


    /**
     * 常量参数 MAP<sonPath/sql placeholder,key>
     * jsonPath
     * key 用来定位常量
     */
    public static final int RUN_RES_PARAMS = 9;

    /**
     * 变量服务参数 MAP<sonPath/sql placeholder,key>
     * jsonPath
     * key 用来定位常量
     */
    public static final int VARIABLE_SERVICE_PARAMS = 10;

    // 判断结果 [path,sucessValue]
    public static final int RES_JUDGE = 111;

}
