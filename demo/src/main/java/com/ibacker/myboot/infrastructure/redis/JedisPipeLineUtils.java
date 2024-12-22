package com.ibacker.myboot.infrastructure.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisClusterConnection;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.util.JedisClusterCRC16;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@Slf4j
public class JedisPipeLineUtils {

    public static void executeJedisClusterPipelined(RedisTemplate redisTemplate, List<String> keys, BiConsumer<Pipeline, List<String>> consumer) {
        // 原redisTemplate do nothing
        redisTemplate.execute(connection -> {
            log.info("connnection started");
            log.info("keys: {}", keys);
            log.info("connection: {}", connection);
            return null;
        }, false, false);
        // 实际进行 jedis pipeline 操作
        RedisConnection redisConnection = redisTemplate.getConnectionFactory().getConnection();

        if (!(redisConnection instanceof JedisClusterConnection)) {
            log.error("非 jedisCluster pipelined 操作");
            throw new RuntimeException("非 jedisCluster pipelined 操作");
        }

        JedisClusterConnection jedisClusterConnection = (JedisClusterConnection) redisConnection;
        JedisCluster jedisCluster = jedisClusterConnection.getNativeConnection();

        // 根据 keys 获取 具体的 Jedis 实例
        Map<Jedis, List<String>> jedisListMap = calculateAndGetJedis(jedisCluster, keys);
        execPipeLine(jedisListMap, consumer);
    }

    private static Map<Jedis, List<String>> calculateAndGetJedis(JedisCluster jedisCluster, List<String> keys) {
        Map<Jedis, List<String>> jedisListMap = new HashMap<>();
        for (String key : keys) {
            int slot = JedisClusterCRC16.getSlot(key);
            Jedis jedis = jedisCluster.getConnectionFromSlot(slot);
            List<String> strings = jedisListMap.get(jedis);
            if (strings == null) {
                strings = new ArrayList<>();
            }
            strings.add(key);
            jedisListMap.put(jedis, strings);
        }
        return jedisListMap;
    }

    private static void execPipeLine(Map<Jedis, List<String>> jedisListMap, BiConsumer<Pipeline, List<String>> consumer) {
        for (Map.Entry<Jedis, List<String>> entry : jedisListMap.entrySet()) {
            Jedis jedis = entry.getKey();
            Pipeline pipeline = jedis.pipelined();
            List<String> slotKeys = entry.getValue();
            try{
                consumer.accept(pipeline, slotKeys);
            } finally {
                pipeline.sync();
                jedis.close();
            }
        }
    }
}
