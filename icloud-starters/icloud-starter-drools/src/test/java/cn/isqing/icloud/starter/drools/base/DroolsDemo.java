package cn.isqing.icloud.starter.drools.base;

import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.KnowledgeBaseFactory;
import org.kie.api.KieServices;
import org.kie.api.definition.type.FactType;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieContainerSessionsPool;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderErrors;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;

import java.nio.charset.StandardCharsets;

/**
 * @author songqingwei
 * @version 1.0
 **/
public class DroolsDemo {

    public void test1() throws InstantiationException, IllegalAccessException {
        //下面规则定义在同一个包路径下,且属于同一个组activation-group
        //具有相同activation-group 属性的规则中只要有一个被执行，其它的规则都不再执行
        //规则1：只动态定义属性类
        String rule1 = "package org.drools.examples\n" +
                "\n" +
                "declare Person\n" +
                "    name:String\n" +
                "    age:int\n" +
                "end\n" +
                "\n" +
                "rule \"rule1\"\n" +
                "activation-group \"test\"\n" +
                "    " +
                "    when\n" +
                "        $p:Person(1!=1)\n" +
                "    then\n" +
                "        System.out.println(\"rule1 pass!\"); \n" +
                "end";
        //规则2
        String rule2 = "package org.drools.examples\n" +
                "\n" +
                "rule \"rule2\"\n" +
                "    activation-group \"test\"\n" +
                "    when\n" +
                "        $p:Person(name==\"2\")\n" +
                "    then\n" +
                "        System.out.println(\"rule2 pass!\"); \n" +
                "end";
        //规则3
        String rule3 = "package org.drools.examples\n" +
                "\n" +
                "rule \"rule3\"\n" +
                "    activation-group \"test\"\n" +
                "    when\n" +
                "        $p:Person(age==30)\n" +
                "    then\n" +
                "        System.out.println(\"rule3 pass!\"); \n" +
                "end";

        //字符串规则转换成drools的资源对象
        Resource resource1 = ResourceFactory.newByteArrayResource(rule1.getBytes(StandardCharsets.UTF_8));
        Resource resource2 = ResourceFactory.newByteArrayResource(rule2.getBytes(StandardCharsets.UTF_8));
        Resource resource3 = ResourceFactory.newByteArrayResource(rule3.getBytes(StandardCharsets.UTF_8));

        //收集编好的规则，对这些规则文件进行编译
        KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        knowledgeBuilder.add(resource1, ResourceType.DRL);
        knowledgeBuilder.add(resource2, ResourceType.DRL);
        knowledgeBuilder.add(resource3, ResourceType.DRL);

        if (knowledgeBuilder.hasErrors()) {
            KnowledgeBuilderErrors errors = knowledgeBuilder.getErrors();
            System.out.println("编译规则有异常：" + errors.toString());
            System.out.println("异常规则数量：" + errors.size());
        }

        //收集知识定义的知识库对象
        InternalKnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
        //将builder的规则包加载到知识库
        knowledgeBase.addPackages(knowledgeBuilder.getKnowledgePackages());

        //属性类实体【业务数据】
        FactType personFactType = knowledgeBase.getFactType("org.drools.examples", "Person");
        Object object = personFactType.newInstance();
        personFactType.set(object, "name", "3");
        personFactType.set(object, "age", 30);
        //接收业务数据 执行规则
        KieSession kieSession = knowledgeBase.newKieSession();
        kieSession.insert(object);
        kieSession.fireAllRules();
        kieSession.dispose();

    }

    public static void test2() throws InstantiationException, IllegalAccessException {
        //下面规则定义在同一个包路径下,且属于同一个组activation-group
        //具有相同activation-group 属性的规则中只要有一个被执行，其它的规则都不再执行
        //规则1：只动态定义属性类
        String rule1 = "package org.drools.examples\n" +
                "\n" +
                "declare Person\n" +
                "    name:String\n" +
                "    age:int\n" +
                "end\n";
        //规则2
        String rule2 = "package org.drools.examples\n" +
                "\n" +
                "rule \"rule2\"\n" +
                "    activation-group \"test\"\n" +
                "    when\n" +
                "        $p:Person(name==\"2\")\n" +
                "    then\n" +
                "        System.out.println(\"rule2 pass!\"); \n" +
                "end";
        //规则3
        String rule3 = "package org.drools.examples\n" +
                "\n" +
                "rule \"rule3\"\n" +
                "    activation-group \"test\"\n" +
                "    when\n" +
                "        $p:Person(age==30)\n" +
                "    then\n" +
                "        System.out.println(\"rule3 pass!\"); \n" +
                "end";

        //字符串规则转换成drools的资源对象
        Resource resource1 = ResourceFactory.newByteArrayResource(rule1.getBytes(StandardCharsets.UTF_8));
        Resource resource2 = ResourceFactory.newByteArrayResource(rule2.getBytes(StandardCharsets.UTF_8));
        Resource resource3 = ResourceFactory.newByteArrayResource(rule3.getBytes(StandardCharsets.UTF_8));

        //收集编好的规则，对这些规则文件进行编译
        KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        knowledgeBuilder.add(resource1, ResourceType.DRL);
        knowledgeBuilder.add(resource2, ResourceType.DRL);
        knowledgeBuilder.add(resource3, ResourceType.DRL);

        if (knowledgeBuilder.hasErrors()) {
            KnowledgeBuilderErrors errors = knowledgeBuilder.getErrors();
            System.out.println("编译规则有异常：" + errors.toString());
            System.out.println("异常规则数量：" + errors.size());
        }

        //收集知识定义的知识库对象
        InternalKnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
        //将builder的规则包加载到知识库
        knowledgeBase.addPackages(knowledgeBuilder.getKnowledgePackages());

        //属性类实体【业务数据】
        FactType personFactType = knowledgeBase.getFactType("org.drools.examples", "Person");
        Object object = personFactType.newInstance();
        personFactType.set(object, "name", "3");
        personFactType.set(object, "age", 30);
        //接收业务数据 执行规则
        KieSession kieSession = knowledgeBase.newKieSession();
        kieSession.insert(object);
        kieSession.fireAllRules();
        kieSession.dispose();

    }

    public static void main(String[] args) {
        try {
            test2();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * drools从7.18版本开始 支持创建KIE session pool用于性能提升。
     */
    public void poolTest() {
        KieServices ks = KieServices.Factory.get();
        KieContainer kc = ks.getKieClasspathContainer();
        //1.从KieContainer创建初始量为10个session的池
        KieContainerSessionsPool pool = kc.newKieSessionsPool(10);
        //2.从池中获取kieSession
        KieSession kieSession = pool.newKieSession();
        //3.kieSession回收
        kieSession.dispose();//pool 回收并重置 Kiesession
        //4.pool的销毁
        pool.shutdown();
        //或者调用KieContainer 的dispose()方法关闭所有通过KieContainer创建的pools
        //kc.dispose();

        //众所周知，stateless KIE session 每次调用excute()方法时，内部会创建stateful KIE session实例，并调用fireAllRules()和dispose()方法。但是，当适用同一个由pool获得的statelessKieSession执行excute时，内部只会从pool中获取KieSession。

    }
}
