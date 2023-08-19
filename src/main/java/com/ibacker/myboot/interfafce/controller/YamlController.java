package com.ibacker.myboot.interfafce.controller;

import com.ibacker.myboot.infrastructure.bean.ResultObject;
import com.ibacker.myboot.infrastructure.util.YamlTool;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("services/yaml")
public class YamlController {

    @GetMapping("parse/{key}")
    public ResultObject getYamlProperties(@PathVariable String key) {
        YamlTool yamlTool = new YamlTool("build/env.yaml");
        Object arr = yamlTool.getValueByKey(key,
                null);
        return ResultObject.success(arr);
    }

    @GetMapping("loadEnv")
    public ResultObject getEnvMap() {
        YamlTool yamlTool = new YamlTool("build/env.yaml");
        Map<String, String> envMap = yamlTool.getEnvList();
        return ResultObject.success(envMap);
    }
}
