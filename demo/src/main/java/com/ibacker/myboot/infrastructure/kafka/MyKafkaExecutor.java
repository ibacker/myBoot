package com.ibacker.myboot.infrastructure.kafka;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class MyKafkaExecutor {
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // 核心线程数
        executor.setMaxPoolSize(10); // 最大线程数
        executor.setQueueCapacity(20); // 等待队列长度
        executor.setThreadNamePrefix("MyThreadPool-"); // 线程名称前缀
        executor.initialize();
        return executor;
    }
}
