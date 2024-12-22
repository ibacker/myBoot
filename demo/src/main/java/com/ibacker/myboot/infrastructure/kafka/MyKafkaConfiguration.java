package com.ibacker.myboot.infrastructure.kafka;

import org.apache.kafka.clients.producer.Producer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class MyKafkaConfiguration {

    @Bean
    public ThreadPoolTaskExecutor myKafkaTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // 核心线程数
        executor.setMaxPoolSize(10); // 最大线程数
        executor.setQueueCapacity(20); // 等待队列长度
        executor.setThreadNamePrefix("MyThreadPool-"); // 线程名称前缀
        executor.initialize();
        return executor;
    }
    @Bean
    MsgPushMethod msgPushMethod(MyKafkaProperties properties, ThreadPoolTaskExecutor myKafkaTaskExecutor) {
        Producer<String, String> producer = new MyKafkaProducer(properties).getProducer();
        return new MsgPushMethod(properties, producer, myKafkaTaskExecutor);
    }
}
