package com.ibacker.myboot.interfafce.controller;

import com.ibacker.myboot.infrastructure.bean.ResultObject;
import com.ibacker.myboot.infrastructure.redis.JedisClusterPipeline;
import com.ibacker.myboot.infrastructure.redis.JedisPipeLineUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

@RestController
@RequestMapping("redis")
public class RedisController {


    @Resource
    RedisTemplate<String,String> redisTemplate;

    @GetMapping("/setvalue")
    public ResultObject hello() {
        // redisTemplate操作内容
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.opsForValue().set("1","2",20000, TimeUnit.MILLISECONDS);


        // redisTemplate 获取 jedisCluster
        Map<String, String> map = getStringMap();
        JedisPipeLineUtils.executeJedisClusterPipelined(redisTemplate, new ArrayList<>(map.keySet()), new BiConsumer<Pipeline, List<String>>() {
            @Override
            public void accept(Pipeline pipeline, List<String> strings) {
                for (String string : strings) {
                    pipeline.setex(string, 30, map.get(string) + "customer");
                }
            }
        });


        List<Object> res = new ArrayList<>();
//        res = testJedisClusterPipeline(map);

        return ResultObject.success(res);
    }

    private static List<Object> testJedisClusterPipeline(Map<String, String> map) {
        // jedisCluster操作内容
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        HashSet jedisClusterNodes = new HashSet();
        jedisClusterNodes.add(new HostAndPort("127.0.0.1",6381));
        JedisCluster jedisCluster = new JedisCluster(jedisClusterNodes,
                2000, // 连接超时时间（毫秒）
                2000, // 读写超时时间（毫秒）
                5,    // 最大重试次数
                "123456", // Redis 密码
                jedisPoolConfig);
        jedisCluster.set("22", "33");
        jedisCluster.get("22");

        //批量设置
        JedisClusterPipeline pipeline = new JedisClusterPipeline(jedisCluster);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            pipeline.setex(entry.getKey(), 30, entry.getValue());
        }
        List<Object> res =  pipeline.syncAndReturnAll();
        return res;
    }

    private static Map<String, String> getStringMap() {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            map.put("testmulti:key"+i, "value"+i);
        }
        return map;
    }



}
