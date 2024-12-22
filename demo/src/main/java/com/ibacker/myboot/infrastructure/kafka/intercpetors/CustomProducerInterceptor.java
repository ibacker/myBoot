package com.ibacker.myboot.infrastructure.kafka.intercpetors;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.internals.RecordHeaders;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
public class CustomProducerInterceptor implements ProducerInterceptor<String, String> {
    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
        // 在这里可以对消息进行修改或记录日志
        String modifiedValue = "Intercepted: " + record.value();
        log.warn("Original Value: {}, Modified Value: {}", record.value(), modifiedValue);

        RecordHeaders headers = new RecordHeaders();
        headers.add("custom", "custom".getBytes(StandardCharsets.UTF_8));

        // 返回一个新的ProducerRecord，也可以直接返回原始record
        return new ProducerRecord<>(record.topic(), record.partition(),  record.key(), modifiedValue, headers);

    }

    // 该方法会在消息被成功发送或者发送失败后调用
    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        if (exception == null) {
            log.warn("Message sent successfully to topic: {}", metadata.topic());
        } else {
            log.warn("Failed to send message: {}", exception.getMessage());
        }
    }

    // 关闭拦截器时调用
    @Override
    public void close() {
        log.warn("CustomProducerInterceptor closed");
    }

    // 用于拦截器的配置初始化
    @Override
    public void configure(Map<String, ?> configs) {
        // 可以在此读取配置
        log.warn("Configuring interceptor with configs: {}", configs);

    }
}
