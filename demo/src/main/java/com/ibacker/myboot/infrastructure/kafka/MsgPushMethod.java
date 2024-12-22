package com.ibacker.myboot.infrastructure.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Slf4j
@Import(MyKafkaProperties.class)
public class MsgPushMethod {
    private final MyKafkaProperties configuration;

    private final Producer<String, String> producer;

    private ThreadPoolTaskExecutor myKafkaTaskExecutor;

    public MsgPushMethod(MyKafkaProperties configuration, Producer<String, String> producer, ThreadPoolTaskExecutor myKafkaTaskExecutor) {
        this.configuration = configuration;
        this.producer = producer;
        this.myKafkaTaskExecutor = myKafkaTaskExecutor;
    }

    public void msgPush(String msg) {
        String topic = configuration.getTopic();
        sendMessage(topic, msg);
    }

    /**
     * 使用线程池提交信息
     */
    public void asyncMsgPush(String msg) {
        String topic = configuration.getTopic();

        myKafkaTaskExecutor.submit(()->{sendMessage(topic, msg);});
    }

    public void sendMessage(String topic, String msg) {
        try {
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, "key");

            producer.send(record, (rm, ex) -> {
                if (ex != null) {
                    log.error("Error sending message {}\n{}", msg, ex.getMessage());
                } else {
                    log.info("Partition for key-value {} is {}", msg, rm.partition());
                }
            });
        } catch (Exception e) {
            log.error("Failed to send message ", e);
        } finally {
            producer.flush();
        }
    }
}
