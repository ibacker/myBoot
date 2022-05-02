package com.ibacker.myboot.controller;

import com.ibacker.myboot.bean.BaseInfo;
import com.ibacker.myboot.bean.ResultObject;
import com.ibacker.myboot.exception.ApiAssert;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("services/interceptor")
public class interceptorController {
    @PostMapping("/interceptorTest")
    public ResultObject interceptorTest(@RequestBody BaseInfo info) {
        ResultObject resultObject = new ResultObject();
        try {
            ApiAssert.notTrue(true,"ed");
            resultObject.setResult("interceptorTest");
        } catch (Exception e) {

            resultObject.setSuccess(false);
            resultObject.setMessage(e.getMessage());
        }
        return resultObject;
    }

}
