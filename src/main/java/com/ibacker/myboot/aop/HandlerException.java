package com.ibacker.myboot.aop;


import com.ibacker.myboot.bean.ResultObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ResourceBundle;

@Aspect
@Slf4j
@Component
public class HandlerException {
//    Logger log = (Logger) LoggerFactory.getLogger(HandlerException.class);

    @Pointcut("execution( * com.ibacker.myboot.controller..*.*(..))")
//    @Pointcut("target(com.tell.service.)")
    public void webException() {
    }

    @Around("webException()")
    public ResultObject handlerControllerMethod(ProceedingJoinPoint proceedingJoinPoint) {
        ResultObject ResultObject;
        try {
            long startTime = System.currentTimeMillis();
            ResultObject = (ResultObject) proceedingJoinPoint.proceed();
            long endTime = System.currentTimeMillis() - startTime;


//            log.info("最后花费的时间为：" + endTime);
            System.out.println("最后花费的时间为："+endTime);
        } catch (Throwable e) {
            System.out.println("hadlerExveption");
//            for (Method method: UserController.class.getDeclaredMethods()) {
//                if(method.getName().equals(proceedingJoinPoint.getSignature().getName())){
//                    LogWrite logWrite = method.getAnnotation(LogWrite.class);
//                    if(logWrite!=null){
//                        System.out.println("Found LogWrite:"+logWrite.user()+" "+logWrite.action() +"处理失败");
//                    }
//                }
//            }
            ResultObject = handlerException(e);
        }

        return ResultObject;
    }
    private ResultObject handlerException(Throwable throwable) {
        ResultObject ResultObject = new ResultObject();
        if (throwable instanceof IllegalArgumentException || throwable instanceof NullPointerException) {
            ResultObject.setSuccess(false);
            ResultObject.setCode(400);
            ResultObject.setMessage("参数或空指针异常");
            log.error("[AOP] IllegalArgumentException or ullPointerException");
        } else if(throwable instanceof RuntimeException){
            ResultObject.setSuccess(false);
            ResultObject.setMessage("运行时异常！");
            ResultObject.setCode(111);
            ResultObject.setMessage("使用AOP实现异常处理，该异常为runtimeException");
            log.error("[AOP] RuntimeException");
        } else {
            ResultObject.setSuccess(false);
            ResultObject.setMessage("未知异常！");
            ResultObject.setCode(222);
            log.error("[AOP] unKnownException");
        }
        return ResultObject;
    }
}
