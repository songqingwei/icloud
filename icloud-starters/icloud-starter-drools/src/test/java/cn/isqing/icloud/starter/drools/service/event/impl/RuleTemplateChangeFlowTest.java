package cn.isqing.icloud.starter.drools.service.event.impl;

import cn.isqing.icloud.starter.drools.common.constants.CommonTextTypeConstants;
import cn.isqing.icloud.starter.drools.common.util.KieUtil;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.internal.conf.ConstraintJittingThresholdOption;
import org.kie.internal.utils.KieHelper;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class RuleTemplateChangeFlowTest {

    @Test
    public void test1(){
        KieServices kieServices = KieServices.Factory.get();
        KieHelper kieHelper = new KieHelper();
        String drl ="package cn.isqing.icloud.starter.drools.rules;\n" +
                "global org.slf4j.Logger log\n" +
                "import cn.isqing.icloud.starter.drools.service.semaphore.util.Allotter\n" +
                "import cn.isqing.icloud.starter.drools.common.util.CronUtil\n" +
                "dialect \"java\"\n" +
                "\n" +
                "rule \"14\"\n" +
                "    no-loop true\n" +
                "\n" +
                "    when\n" +
                "      $data : Data( CronUtil.isSatisfied(\"* * * * ?\",busiDate) && (v1 > 1))\n" +
                "    then\n" +
                "       log.info(\"命中规则：1:14\");\n" +
                "       $data.setRuleId(14L);\n" +
                "       $data.setTargetRes(\n" +
                "            Allotter.getTargetId($data.getCoreId(),14L,$data.getV1().toString())\n" +
                "       );\n" +
                "end\n";
        byte[] b1 = drl.getBytes();
        Resource resource = kieServices.getResources().newByteArrayResource(b1);
        kieHelper.addResource(resource, ResourceType.DRL);
        String drl2 ="package cn.isqing.icloud.starter.drools.rules;\n" +
                "declare Data\n" +
                "   v1: java.math.BigInteger\n" +
                "    targetRes:cn.isqing.icloud.common.api.dto.Response\n" +
                "    busiDate:java.time.LocalDate\n" +
                "    ruleId:Long\n" +
                "    coreId:Long\n" +
                "end";
        byte[] b2 = drl2.getBytes();
        Resource resource2 = kieServices.getResources().newByteArrayResource(b2);
        kieHelper.addResource(resource2, ResourceType.DRL);


        KieBaseConfiguration kieBaseConfiguration = kieServices.newKieBaseConfiguration();
        //禁用jittingThreshold(阈值默认20，设为-1)
        kieBaseConfiguration.setOption(ConstraintJittingThresholdOption.get(-1));
        KieBase kieBase = kieHelper.build(kieBaseConfiguration);

    }


}