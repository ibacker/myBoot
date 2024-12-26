package com.ibacker.gateway.filters.global;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
@Slf4j
@Component
public class LoggingGlobalFilter implements GlobalFilter, Ordered {

    private final List<HttpMessageReader<?>> messageReaders;

    public LoggingGlobalFilter(ServerCodecConfigurer codecConfigurer) {
        this.messageReaders = codecConfigurer.getReaders();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 原始请求对象
        ServerHttpRequest originalRequest = exchange.getRequest();

        return ServerWebExchangeUtils.cacheRequestBodyAndRequest(exchange, (cachedRequest) -> {
            // 构建 ServerRequest 来解析请求体
            ServerRequest serverRequest = ServerRequest.create(
                    exchange.mutate().request(cachedRequest).build(), messageReaders
            );

            return serverRequest.bodyToMono(String.class)
                    .defaultIfEmpty("")
                    .doOnNext(body -> {
                        // 打印请求体内容（限制日志大小）
                        String truncatedBody = body.length() > 500 ? body.substring(0, 500) + "..." : body;
                        log.info("请求路径: {}, 请求体: {}", originalRequest.getPath(), truncatedBody);
                    })
                    .flatMap(body -> {
                        if (cachedRequest == originalRequest) {
                            // 如果请求未被缓存，继续使用原始请求
                            return chain.filter(exchange);
                        }

                        // 使用缓存的请求重新构建 exchange
                        ServerHttpRequest newRequest = (ServerHttpRequest) exchange.getAttributes()
                                .get(ServerWebExchangeUtils.CACHED_SERVER_HTTP_REQUEST_DECORATOR_ATTR);

                        return chain.filter(exchange.mutate().request(newRequest).build());
                    });
        });
    }

    @Override
    public int getOrder() {
        return -1; // 优先级，数字越小优先级越高
    }
}

