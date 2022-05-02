package com.ibacker.myboot.Interceptor;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpRequestHandler;
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
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        MDC.put("result", "OK");
        log.info("进入到拦截器中:postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
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
        log.info("进入到拦截器中: afterComponent");
    }

}
