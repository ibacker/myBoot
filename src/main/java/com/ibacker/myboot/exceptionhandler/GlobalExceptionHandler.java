package com.ibacker.myboot.exceptionhandler;


import com.ibacker.myboot.bean.ResultObject;
import com.ibacker.myboot.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 获取错误码
     */
    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }

    /**
     * 捕获接口ApiException异常，并返回自定义的异常信息和格式的json
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = ApiException.class)
    @ResponseBody
    public ResultObject jsonErrorHandler(ApiException e) {
        ResultObject ResultObject = new ResultObject();
        ResultObject.setSuccess(false);
        ResultObject.setCode(e.getCode());
        ResultObject.setMessage(e.getMessage()+"globalException");
        return ResultObject;
    }

    /**
     * 捕获其他Exception异常，并返回一个带有错误码和错误信息的视图
     */
    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest request, Exception e) throws Exception {
        e.printStackTrace();
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e.getMessage());
        mav.addObject("errorCode", mav.getStatus());
        mav.setViewName("error/error");
        return mav;
    }

    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public ResultObject nullPointerErrorHandler(HttpServletRequest request, NullPointerException e) {
        ResultObject ResultObject = new ResultObject();
        ResultObject.setSuccess(false);
        ResultObject.setCode(000);
        ResultObject.setMessage("空指针异常");
        return ResultObject;
    }

    @ExceptionHandler(value = ClassCastException.class)
    @ResponseBody
    public ResultObject ClassCastErrorHandler(HttpServletRequest request, NullPointerException e) {
        ResultObject ResultObject = new ResultObject();
        ResultObject.setSuccess(false);
        ResultObject.setCode(000);
        ResultObject.setMessage("类型转换异常");
        return ResultObject;
    }
}

