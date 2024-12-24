package com.ibacker.gateway.filters;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class GlobalLoggingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(org.springframework.web.server.ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String originalPath = request.getURI().toString(); // 原始请求地址
        String targetPathPath = exchange.getAttribute("org.springframework.cloud.gateway.support.ServerWebExchangeUtils.gatewayRoute").toString(); // 目标地址

        System.out.println("Original Request URL: " + originalPath);
//        System.out.println("Forwarding to Target URL: " + targetPath);

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 1; // 设置过滤器优先级
    }
}
