package com.ibacker.myboot.infrastructure.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;

@Slf4j
@Component
@Configuration
public class ZooKeeperClient {

    @Value("${zooKeeper.connectionStr:localhost:2181}")
    private String zooKeeperConnectionStr;
    @Value("${zooKeeper.defaultNameSpace:test}")
    private String zooKeeperDefaultNameSpace;

    RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
    CuratorFramework client = null;

    @PostConstruct
    private void initZooKeeperClient() {
        this.client = CuratorFrameworkFactory.builder()
                .connectString(zooKeeperConnectionStr)
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .namespace(zooKeeperDefaultNameSpace)
                .build();
        this.client.start();
    }

    public CuratorFramework getClient() {
        if (ObjectUtils.isEmpty(client)) {
            initZooKeeperClient();
        }
        return client;
    }

}
