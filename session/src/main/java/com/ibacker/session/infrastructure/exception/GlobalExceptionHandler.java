package com.ibacker.session.infrastructure.exception;


import com.ibacker.session.infrastructure.bean.ResultObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@ControllerAdvice
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


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity<>("Runtime error: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(VerificationCodeMismatchException.class)
    public ResultObject handleVerificationCodeMismatchException(VerificationCodeMismatchException ex) {
        ResultObject ResultObject = new ResultObject();
        ResultObject.setSuccess(false);
        ResultObject.setCode(400);
        ResultObject.setMessage(ex.getMessage());
        return ResultObject;
    }
}

