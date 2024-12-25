package com.ibacker.gateway.filters.global;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoggingGlobalFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("Executing LoggingGlobalFilter - Pre");
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            System.out.println("Executing LoggingGlobalFilter - Post");
        }));
    }

    @Override
    public int getOrder() {
        return -1; // 优先级，数字越小优先级越高
    }
}
