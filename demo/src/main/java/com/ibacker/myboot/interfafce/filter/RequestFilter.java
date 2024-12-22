package com.ibacker.myboot.interfafce.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

@Component
@Slf4j
public class RequestFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        Filter.super.init(filterConfig);
        log.info("***** Request Filter init *****");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(servletRequest, servletResponse);
        // 在视图页面返回给客户端之前执行，但执行顺序在Interceptor之后
        log.info("***** do request Filter after *****");
    }

    @Override
    public void destroy() {
        log.info("***** do request Filter destroy *****");
        Filter.super.destroy();
    }
}
