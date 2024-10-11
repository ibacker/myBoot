package com.ibacker.myboot.interfafce.controller;

import com.ibacker.myboot.infrastructure.bean.ResultObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RequestAnnotationController {

    @GetMapping(path = "/greeting")
    @ResponseBody
    public ResultObject index() {
        ResultObject resultObject = new ResultObject();
        resultObject.setMessage("greeting");
        return resultObject;
    }
}
