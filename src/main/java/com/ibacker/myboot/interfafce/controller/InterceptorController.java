package com.ibacker.myboot.interfafce.controller;

import com.ibacker.myboot.domain.interceptor.BaseInfo;
import com.ibacker.myboot.infrastructure.aop.AuthorityVerifyAnnotation;
import com.ibacker.myboot.infrastructure.bean.ResultObject;
import com.ibacker.myboot.infrastructure.util.exception.ApiAssert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("services/interceptor")
public class InterceptorController {
    @PostMapping("/interceptorTest")
    @AuthorityVerifyAnnotation(verify = true)
    public ResultObject interceptorTest(@RequestBody BaseInfo info) {
        ResultObject resultObject = new ResultObject();

        ApiAssert.notTrue(true, "not true");
        resultObject.setResult("interceptorTest");

        return resultObject;
    }


    @PostMapping("/interceptorUA")
    public ResultObject interceptorTestUA(@RequestBody BaseInfo info) {
        ResultObject resultObject = new ResultObject();
        try {
            ApiAssert.notTrue(true, "not true");
            resultObject.setResult("interceptorTest");
        } catch (Exception e) {

            resultObject.setSuccess(false);
            resultObject.setMessage(e.getMessage());
        }
        return resultObject;
    }

}
