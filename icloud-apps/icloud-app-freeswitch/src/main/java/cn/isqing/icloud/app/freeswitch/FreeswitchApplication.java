package cn.isqing.icloud.app.freeswitch;

import cn.isqing.icloud.app.freeswitch.utils.ClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.freeswitch.esl.client.inbound.Client;
import org.freeswitch.esl.client.inbound.InboundConnectionFailure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@SpringBootApplication
@EnableScheduling
@Slf4j
public class FreeswitchApplication {

    public static void main(String[] args) {
        SpringApplication.run(FreeswitchApplication.class, args);
    }

    @Bean("executor")
    public Executor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数为10
        executor.setCorePoolSize(10);
        // 设置最大线程数为50
        executor.setMaxPoolSize(50);
        // 设置队列容量为0，即不使用队列
        executor.setQueueCapacity(0);
        // 设置拒绝策略为抛出异常
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        // 初始化线程池
        executor.initialize();
        return executor;
    }


    @Scheduled(fixedRate = 5000)
    public void checkClient(){
        Client client = ClientUtil.client;
        if(!client.canSend()){
            try {
                client.connect(new InetSocketAddress("localhost", 8021), "ClueCon", 10);
            } catch (InboundConnectionFailure e) {
                log.error(e.getMessage(),e);
            }
        }
    }



}
