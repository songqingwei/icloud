package cn.isqing.icloud.starter.drools.common.constants;

import cn.isqing.icloud.starter.drools.common.util.KieUtil;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public abstract class FactDataConstants {
    private FactDataConstants() {
    }
    public static final String  BUSI_DATE = "busiDate";
    public static final String  CORE_ID = "coreId";
    public static final String  RULE_ID = "ruleId";
    public static final String  TARGET_ID = "targetId";

    public static final String  DRL_TEMPLATE = "package " + KieUtil.PACKAGE_NAME + ";\n" +
            "declare Data\n" +
            "   {}\n"+
            "    targetRes:cn.isqing.icloud.utils.dto.Response\n" +
            "    busiDate:java.time.LocalDate\n" +
            "    ruleId:Long\n" +
            "    coreId:Long\n" +
            "end";
}
