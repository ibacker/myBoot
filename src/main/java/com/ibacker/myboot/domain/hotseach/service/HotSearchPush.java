package com.ibacker.myboot.domain.hotseach.service;

import com.ibacker.myboot.domain.hotseach.entity.zhiHuHot;
import com.ibacker.myboot.infrastructure.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class HotSearchPush {
    @Resource
    KafkaProducer<String, String> hotSearchKafkaProducer;

    public void pushZhiHuHotSearch(zhiHuHot hotInfo) {
        hotSearchKafkaProducer.send(new ProducerRecord<>("demo", JsonUtil.objectToJson(hotInfo)),
                (recordMetadata, e) -> log.info("produce success with metaData: {}", recordMetadata));
    }

}
