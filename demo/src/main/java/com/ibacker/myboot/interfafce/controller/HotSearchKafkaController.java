package com.ibacker.myboot.interfafce.controller;

import com.ibacker.myboot.application.HotSearchService;
import com.ibacker.myboot.domain.hotseach.entity.zhiHuHot;
import com.ibacker.myboot.infrastructure.bean.ResultObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("services/kafka")
public class HotSearchKafkaController {

    @Resource
    HotSearchService hotSearchService;


    @PostMapping("zhihu")
    ResultObject pushZhiHuINfo(@RequestBody zhiHuHot info) {
        ResultObject resultObject = new ResultObject();
        try {
//            hotSearchService.pushZhiHuInfo(info);
            hotSearchService.pushZhiHuHotSearch(info);
        } catch (Exception e) {
            ResultObject.error(e.getMessage());
        }
        return resultObject;
    }

}
