package com.ibacker.gateway.filters.global;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("Executing CustomGlobalFilter - Pre");
        // 打印请求路径
        System.out.println("CustomGlobalFilter: " + exchange.getRequest().getURI().getPath());

        // 添加请求头
        exchange.getRequest().mutate().header("X-Custom-Header", "MyGateway").build();

        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    // 响应后处理逻辑
                    System.out.println("CustomGlobalFilter Response status code: " + exchange.getResponse().getStatusCode());
                    System.out.println("Executing CustomGlobalFilter - Post");

                }));
    }

    @Override
    public int getOrder() {
        return 2; // 优先级，数字越小优先级越高
    }
}
