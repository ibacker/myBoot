package com.ibacker.myboot.application;

import com.ibacker.myboot.domain.hotseach.entity.zhiHuHot;
import com.ibacker.myboot.domain.hotseach.service.HotSearchPush;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class HotSearchService {


    @Resource
    HotSearchPush hotSearchPush;

    public void pushZhiHuInfo(zhiHuHot hotInfo) {
        hotSearchPush.pushZhiHuHotSearch(hotInfo);
    }
}
