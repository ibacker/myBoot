package com.ibacker.myboot.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class KafkaPropertyConfig {
    private String bootstrapServers;
    private String hotSearchTopic;

    private String userName;
    private String password;
}
