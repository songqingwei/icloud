package cn.isqing.icloud.starter.variable.common.constants;

/**
 * 值不要重复
 * 不用枚举是防止太多了扩展不变
 * 太多了的则可以新建多个常量类
 *
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public class CommonTextTypeConstants {
    private CommonTextTypeConstants() {
    }
    // rule_template 相关--------------------------
    // 前端传过来的规则内容
    public static final int RULE_CONTENT_H5 = 1;
    // 格式化后的规则内容
    public static final int RULE_CONTENT = 2;
    // 规则涉及的变量集合
    public static final int RULE_VARIABLE_MAP = 3;
    // 目标比例-json
    public static final int TARGET_RATIO = 4;
    // 目标名称-json
    public static final int TARGET_NAME = 5;
    // rule_template 相关 end--------------------------


    public static final int DATA_SOURCE_CINFIG = 11;

}
