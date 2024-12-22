package com.ibacker.myboot.infrastructure.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.util.ObjectUtils;

import java.util.Properties;

@Slf4j
public class MyKafkaProducer {
    private Producer<String, String> singletonProducer;
    private final MyKafkaProperties myKafkaProperties;

    public MyKafkaProducer(MyKafkaProperties myKafkaProperties) {
        this.myKafkaProperties = myKafkaProperties;
    }

    public Producer<String, String> getProducer() {
        try {
            if (ObjectUtils.isEmpty(singletonProducer)) {
                Properties props = new Properties();
                props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                        myKafkaProperties.getBootStrapServer());
                props.put(ProducerConfig.CLIENT_ID_CONFIG, myKafkaProperties.getClientId());
                props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                        StringSerializer.class);
                props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                        StringSerializer.class);
                props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG,
                        1000);
                props.put(ProducerConfig.RETRIES_CONFIG,
                        0);

                props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG,
                        1000);

                props.put("max.block.ms",10000);

                props.setProperty(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, "com.ibacker.myboot.infrastructure.kafka.intercpetors.CustomProducerInterceptor");


                singletonProducer = new KafkaProducer<>(props);
            }
            return singletonProducer;
        } catch (Exception e) {
            log.error("构建producer异常: {}", e.getMessage(), e);
        }
        return null;
    }
}
