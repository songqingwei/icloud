package cn.isqing.icloud.policy.engine;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class PolicyEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(PolicyEngineApplication.class, args);
    }


}
