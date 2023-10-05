package com.ibacker.myboot.interfafce.controller;

import com.ibacker.myboot.application.ZooKeeperPracticeService;
import com.ibacker.myboot.infrastructure.bean.ResultObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/zoo")
public class ZooKeeperController {
    @Resource
    ZooKeeperPracticeService zooKeeperPracticeService;

    @PutMapping("/getChildrenOfPath")
    public ResultObject get(@RequestParam(name = "path") String path) {
        ResultObject resultObject = new ResultObject();
        try {
            resultObject.setResult(zooKeeperPracticeService.getChildren(path));
        } catch (Exception e) {
            log.error("error: {}", e.getMessage(), e);
            return ResultObject.error();
        }
        return resultObject;
    }
}
