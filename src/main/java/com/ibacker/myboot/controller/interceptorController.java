package com.ibacker.myboot.controller;

import com.ibacker.myboot.aop.AuthorityVerifyAnnotation;
import com.ibacker.myboot.bean.BaseInfo;
import com.ibacker.myboot.bean.ResultObject;
import com.ibacker.myboot.exception.ApiAssert;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("services/interceptor")
public class interceptorController {
    @PostMapping("/interceptorTest")
    @AuthorityVerifyAnnotation(verify = true)
    public ResultObject interceptorTest(@RequestBody BaseInfo info) {
        ResultObject resultObject = new ResultObject();

            ApiAssert.notTrue(true,"not true");
            resultObject.setResult("interceptorTest");

        return resultObject;
    }


    @PostMapping("/interceptorUA")
    public ResultObject interceptorTestUA(@RequestBody BaseInfo info) {
        ResultObject resultObject = new ResultObject();
        try {
            ApiAssert.notTrue(true,"not true");
            resultObject.setResult("interceptorTest");
        } catch (Exception e) {

            resultObject.setSuccess(false);
            resultObject.setMessage(e.getMessage());
        }
        return resultObject;
    }

}
