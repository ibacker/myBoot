package com.ibacker.myboot.application;

import com.ibacker.myboot.infrastructure.zookeeper.ZooKeeperClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ZooKeeperPracticeService {
    @Resource
    ZooKeeperClient zooKeeperClient;

    public List<String> getChildren(String path) throws Exception {
        if (StringUtils.isEmpty(path)) {
            path = "/";
        }
        return zooKeeperClient.getClient().getChildren().forPath(path);
    }
}
