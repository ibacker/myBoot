//package com.ibacker.session.config;
//
//
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
//import org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration;
//
//@Configuration
//@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 180)
//@ConditionalOnProperty(prefix = "spring.session.store-type", value = "redis", matchIfMissing = false)
//public class RedisSessionConfig {
//    // 手动创建 RedisHttpSessionConfiguration Bean
//    @Bean
//    @Primary
//    public RedisHttpSessionConfiguration redisHttpSessionConfiguration() {
//        RedisHttpSessionConfiguration redisConfig = new RedisHttpSessionConfiguration();
//        redisConfig.setMaxInactiveIntervalInSeconds(180); // 设置 Session 过期时间（单位：秒，默认 1800 秒）
//        return redisConfig;
//    }
//
//}
