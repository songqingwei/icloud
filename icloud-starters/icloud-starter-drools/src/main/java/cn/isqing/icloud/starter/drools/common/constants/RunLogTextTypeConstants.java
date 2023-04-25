package cn.isqing.icloud.starter.drools.common.constants;

/**
 * 值不要重复
 * 不用枚举是防止太多了扩展不变
 * 太多了的则可以新建多个常量类
 *
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public class RunLogTextTypeConstants {
    private RunLogTextTypeConstants() {
    }

    // Map<String,Object>
    public static final int VC_RES_MAP = 1;
    // Map<String,Object>
    public static final int INPUT_PARAMS = 2;
    public static final int RUN_RES_MAP = 3;


}
