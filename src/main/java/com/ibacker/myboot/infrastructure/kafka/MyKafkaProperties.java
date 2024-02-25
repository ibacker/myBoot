package com.ibacker.myboot.infrastructure.kafka;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "my.kafka")
@Configuration
public class MyKafkaProperties {
    private String bootStrapServer;
    private String clientId;

    private String topic;
}
