package com.ibacker.myboot.Interceptor;

import com.ibacker.myboot.aop.AuthorityVerifyAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
public class AuthorityInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("进入到拦截器中:preHandle");

        if (handler instanceof HandlerMethod) {

            HandlerMethod handlerMethod = (HandlerMethod) handler;
            AuthorityVerifyAnnotation annotation = handlerMethod.getMethodAnnotation(AuthorityVerifyAnnotation.class);
            // 只处理有注解的请求
            if (!ObjectUtils.isEmpty(annotation) && annotation.verify()) {
                MDCPreLog(request, response, handler);
                return true;
            }
        }


        return true;
    }

    /**
     * postHandle 会在handler处理完成之后执行，而afterCompletion会在整个请求处理完毕之后执行。也就是执行
     * <p>
     * 一个典型的例子是如果在controller中抛错，那么postHandle不会执行，而afterCompletion会执行
     */

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        MDC.put("result", "OK");
        log.info("进入到拦截器中:postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            AuthorityVerifyAnnotation annotation = handlerMethod.getMethodAnnotation(AuthorityVerifyAnnotation.class);
            // 只处理有注解的请求
            if (!ObjectUtils.isEmpty(annotation) && annotation.verify()) {
                MDCAfterLog();
            }
        }
        log.info("进入到拦截器中: afterComponent");
    }

    /**
     * 请求日志
     */

    private void MDCPreLog(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestId = UUID.randomUUID().toString();
        String startTime = Long.toString(new Date().getTime());
        String uri = request.getRequestURI();
        String params = request.getParameterMap().entrySet().stream()
                .map(e -> Arrays.stream(e.getValue()).map(v -> e.getKey() + "=" + v)
                        .reduce((v1, v2) -> v1 + "&" + v2).orElse("")
                )
                .reduce((p1, p2) -> p1 + "&" + p2)
                .orElse("");
        String method = request.getMethod();
        String ip = StringUtils.isEmpty(request.getHeader("X-FORWARDED-FOR")) ? request.getLocalAddr() : request.getHeader("X-FORWARDED-FOR");
        String userAgent = request.getHeader("user-agent");
        log.info("new request: requestId: {}, startTime:{}, uri: {}, params: {}, method: {}, ip: {}, user-agent: {}",
                requestId, startTime, uri, params, method, ip, userAgent);
        MDC.put("requestId", requestId);
        MDC.put("startTime", startTime);
        MDC.put("uri", uri);
        MDC.put("params", params);
        MDC.put("method", method);
        MDC.put("ip", ip);
        MDC.put("userAgent", userAgent);
    }

    /**
     * 返回日志
     */
    private void MDCAfterLog() {
        String endTime = Long.toString(new Date().getTime());
        log.info("request end: requestId: {}, endTime:{}, result: {}", MDC.get("requestId"), endTime, MDC.get("result"));

        MDC.remove("requestId");
        MDC.remove("startTime");
        MDC.remove("uri");
        MDC.remove("params");
        MDC.remove("method");
        MDC.remove("ip");
        MDC.remove("userAgent");
        MDC.remove("result");
    }

}
