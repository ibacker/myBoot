package com.ibacker.zuul;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class PreLogFilter extends ZuulFilter {


    @Override
    public String filterType() {
        return "pre"; // 过滤器类型：pre（请求前）、route（路由时）、post（响应后）、error（出错时）
    }

    @Override
    public int filterOrder() {
        return 1; // 优先级，数字越小优先级越高
    }

    @Override
    public boolean shouldFilter() {
        return true; // 是否启用过滤器
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        log.info("QueryString: {}", request.getQueryString());
        log.info("Query: {}", request.getParameter("1"));
        log.info("请求方法: {}, 请求URL: {}", request.getMethod(), request.getRequestURL().toString() + request.getQueryString());
        return null;
    }
}
