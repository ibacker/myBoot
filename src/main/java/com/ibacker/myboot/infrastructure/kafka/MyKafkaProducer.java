package com.ibacker.myboot.infrastructure.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MyKafkaProducer {

    @Resource
    KafkaProperties properties;

    @Bean
    public KafkaProducer<String, String> hotSearchKafkaProducer() {
        return new KafkaProducer<>(properties.buildProducerProperties());
    }
}
