package cn.isqing.icloud.starter.drools.config;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

public class MyBeanNameGenerator extends AnnotationBeanNameGenerator {

    private String pre = System.getProperty("i.drools.beanName.pre", "iDrools");
    private String preLowerCase;

    public MyBeanNameGenerator() {
        this.preLowerCase = pre.toLowerCase();
    }

    @Override
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        if (definition instanceof AnnotatedBeanDefinition) {
            String beanName = determineBeanNameFromAnnotation((AnnotatedBeanDefinition) definition);
            if (StringUtils.hasText(beanName)) {
                // Explicit bean name found.
                return beanName;
            }
        }
        // Fallback: generate a unique default bean name.
        return buildDefaultBeanName(definition, registry);
    }


    @Override
    protected String buildDefaultBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        String beanClassName = definition.getBeanClassName();
        Assert.state(beanClassName != null, "No bean class name set");
        String name = ClassUtils.getShortName(beanClassName);
        if (name.length() == 0) {
            return name;
        }
        if (name.length() >= pre.length() && name.substring(0, pre.length() - 1).toLowerCase().equals(preLowerCase)) {
            return name;
        }
        return pre + name;
    }


}
