package cn.isqing.icloud.starter.drools.base.drt;

import lombok.extern.slf4j.Slf4j;
import org.drools.template.DataProvider;
import org.drools.template.DataProviderCompiler;
import org.drools.template.ObjectDataCompiler;
import org.drools.template.objects.ArrayDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@RunWith(SpringRunner.class)
@TestPropertySource("classpath:application.properties")//不支持.yml文件
// @ContextConfiguration
@Slf4j
public class DrlTest {

    @Test
    public void test1(){
        List<Map<String, String>> ruleAttributes = new ArrayList<>();
        Map<String, String> rule1 = new HashMap<>();
        rule1.put("id","1");
        rule1.put("domain","domain1");
        rule1.put("busiCode","code1");
        rule1.put("condition","1==1");
        rule1.put("orgId","3");
        rule1.put("cron","* * * * * ?");
        rule1.put("ref","days");
        ruleAttributes.add(rule1);
        ObjectDataCompiler compiler = new ObjectDataCompiler();
        String generatedDRL;
        try {
            String url = ResourceUtils.getURL("classpath:").getPath();
            FileInputStream inputStream = new FileInputStream(ResourceUtils.getFile("classpath:RuleTemplate.drt"));
            generatedDRL = compiler.compile(ruleAttributes, inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        String dataStr = "package cn.isqing.icloud.starter.drools.rules;\n" +
                "declare Data\n" +
                "    targetId:Long\n" +
                "    busiDate:java.util.Date\n" +
                "    ruleId:Long\n" +
                "end";
        KieServices kieServices = KieServices.Factory.get();

        KieHelper kieHelper = new KieHelper();
        //multiple such resoures/rules can be added
        byte[] b1 = generatedDRL.getBytes();
        Resource resource1 = kieServices.getResources().newByteArrayResource(b1);
        kieHelper.addResource(resource1, ResourceType.DRL);

        KieBase kieBase = kieHelper.build();
        // kieBase.removeRule();
        //规则应用：
        KieSession kieSession = kieBase.newKieSession();
        // kieSession.setGlobal("myGlobal", myGlobal);
        // kieSession.insert(abc);
        int numberOfRulesFired = kieSession.fireAllRules();
        kieSession.dispose();


    }

    /**
     * 数据模版入参
     */
    public void test2(){
        InputStream templateStream = null;
        try {
            templateStream = new FileInputStream("/rules/SimpleTemplateExample.drt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        // @{row.rowNumber}=数组下标
        DataProvider data = new ArrayDataProvider(new String[][]{
                new String[]{"第1个定义字段对应值", "第2个定义字段对应值"}
        });
        DataProviderCompiler converter = new DataProviderCompiler();
        String drl = converter.compile(data, templateStream);
    }

    @Test
    public void test3(){
        String dataStr = "package cn.isqing.icloud.starter.drools.rules;\n" +
                "declare Data_FMD-1\n" +
                "    targetId:Long\n" +
                "    busiDate:java.util.Date\n" +
                "    ruleId:Long\n" +
                "end";
        KieServices kieServices = KieServices.Factory.get();

        KieHelper kieHelper = new KieHelper();
        //multiple such resoures/rules can be added
        byte[] b1 = dataStr.getBytes();
        Resource resource1 = kieServices.getResources().newByteArrayResource(b1);
        kieHelper.addResource(resource1, ResourceType.DRL);

        KieBase kieBase = kieHelper.build();
        //规则应用：
        KieSession kieSession = kieBase.newKieSession();
        // kieSession.setGlobal("myGlobal", myGlobal);
        // kieSession.insert(abc);
        int numberOfRulesFired = kieSession.fireAllRules();
        kieSession.dispose();
    }

}
