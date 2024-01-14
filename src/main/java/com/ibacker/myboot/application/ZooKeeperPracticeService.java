package com.ibacker.myboot.application;

import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ZooKeeperPracticeService {
    @Resource
    CuratorFramework zookeeperClient;

    public List<String> getChildren(String path) throws Exception {
        if (StringUtils.isEmpty(path)) {
            path = "/";
        }
        return zookeeperClient.getChildren().forPath(path);
    }
}
