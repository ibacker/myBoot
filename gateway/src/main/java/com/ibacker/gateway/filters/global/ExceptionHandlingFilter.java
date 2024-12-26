package com.ibacker.gateway.filters.global;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class ExceptionHandlingFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange)
                .onErrorResume(throwable -> {
                    log.error("捕获到异常: {}", throwable.getMessage());

                    // 自定义错误响应
                    String errorResponse = "{\"message\":\"服务异常，请稍后再试！\",\"error\":\"" + throwable.getMessage() + "\"}";

                    exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                    exchange.getResponse().getHeaders().add("Content-Type", "application/json");

                    DataBuffer buffer = exchange.getResponse()
                            .bufferFactory()
                            .wrap(errorResponse.getBytes(StandardCharsets.UTF_8));

                    return exchange.getResponse().writeWith(Mono.just(buffer));
                });
    }



}
