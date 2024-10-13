package com.ibacker.myboot.interfafce.controller;

import com.ibacker.myboot.infrastructure.bean.ResultObject;
import com.ibacker.myboot.infrastructure.redis.JedisClusterPipeline;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("redis")
public class RedisController {


    @Resource
    RedisTemplate<String,String> redisTemplate;

    @GetMapping("/setvalue")
    public ResultObject hello() {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.opsForValue().set("1","2",20000, TimeUnit.MILLISECONDS);

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
        Map<String, String> map = getStringMap();

        JedisClusterPipeline pipeline = new JedisClusterPipeline(jedisCluster);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            pipeline.setex(entry.getKey(), 30, entry.getValue());
        }
        List<Object> res =  pipeline.syncAndReturnAll();


        return ResultObject.success(res);
    }

    private static Map<String, String> getStringMap() {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < 1000; i++) {
            map.put("testmulti:key"+i, "value"+i);
        }
        return map;
    }



}
