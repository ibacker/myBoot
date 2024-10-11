package com.ibacker.myboot.domain.hotseach.service;

import com.ibacker.myboot.domain.hotseach.entity.zhiHuHot;
import com.ibacker.myboot.infrastructure.kafka.MsgPushMethod;
import com.ibacker.myboot.infrastructure.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class HotSearchPush {

    @Resource
    MsgPushMethod msgPushMethod;

    @Resource
    KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.template.default-topic:demo}")
    private String topic;


    /**
     * 自定义的 kafka producer 发送消息
     * @param hotInfo
     */
    public void pushZhiHuHotSearch(zhiHuHot hotInfo) {
        msgPushMethod.asyncMsgPush(JsonUtil.objectToJson(hotInfo));
    }


    /**
     * 使用 kafkaTemplate 发送消息
     * @param hotInfo
     */
    public void pushZhiHuHotSearchKafka(zhiHuHot hotInfo) {
        kafkaTemplate.send(topic, JsonUtil.objectToJson(hotInfo)).addCallback(
                success->{
                    log.info("push msg success");
                },
                failure->{
                    log.error("push msg failure", failure);
                }
        );
    }

    /**
     * kafka 消费 消息
     * @param record
     */
    @KafkaListener(topics = "${spring.kafka.consumer.properties.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void ConsumerZhiHuInfo(ConsumerRecord<String, String> record) {
        System.out.println("Received message: " + record.value());
    }


}
