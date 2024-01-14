package com.ibacker.myboot.infrastructure.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class ZooKeeperConfiguration {

    @Value("${zooKeeper.connectionStr:localhost:2181}")
    private String connectString;
    @Value("${zooKeeper.defaultNameSpace:test}")
    private String namespace;

    @Value("${zooKeeper.zooKeeperSessionTimeoutMs:5000}")
    private int sessionTimeoutMs;

    @Value("${zooKeeper.connectionTimeoutMs:5000}")
    private int connectionTimeoutMs;

    CuratorFramework client = null;

    @Bean
    CuratorFramework zookeeperClient() {
        try {
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
            this.client = CuratorFrameworkFactory.builder()
                    .connectString(connectString)
                    .sessionTimeoutMs(sessionTimeoutMs)
                    .connectionTimeoutMs(connectionTimeoutMs)
                    .retryPolicy(retryPolicy)
                    .namespace(namespace)
                    .build();
            this.client.start();
            // 此时可以检查连接状态，处理连接是否成功的逻辑
            if (this.client.blockUntilConnected(10, TimeUnit.SECONDS)) {
                log.info("Connected to ZooKeeper");
            } else {
                log.error("Failed to connect to Zookeeper!");
                this.client.close();
            }

        } catch (Exception e) {
            // 在适当的时候关闭 CuratorFramework 客户端
            client.close();
            log.error("zookeeper链接创建失败");
        } finally {
            if (client.getZookeeperClient().isConnected()) {
                System.out.println("Connected to ZooKeeper!");
            } else {
                System.err.println("Failed to connect to ZooKeeper!");
            }
        }
        return this.client;
    }

}
