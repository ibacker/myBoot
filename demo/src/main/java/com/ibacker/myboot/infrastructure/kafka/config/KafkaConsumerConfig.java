package com.ibacker.myboot.infrastructure.kafka.config;


import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConsumerConfig {

//    // Kafka 集群 1 消费者配置
//    @Bean
//    public Map<String, Object> kafkaConsumerConfig1() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka-cluster1:9092");
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group1");
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        return props;
//    }
//
//    @Bean
//    public ConsumerFactory<String, String> consumerFactory1() {
//        return new DefaultKafkaConsumerFactory<>(kafkaConsumerConfig1());
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory1() {
//        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory1());
//        return factory;
//    }


}
