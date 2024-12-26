package com.ibacker.gateway.filters.global;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class LoggingResponseFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {


        // 获取请求信息
        String requestPath = exchange.getRequest().getURI().getPath();
        long startTime = System.currentTimeMillis();

        // 包装响应对象
        ServerHttpResponse originalResponse = exchange.getResponse();
        DataBufferFactory bufferFactory = originalResponse.bufferFactory();

        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
            @Override
            public Mono<Void> writeWith(@Nullable Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;

                    return super.writeWith(fluxBody.map(dataBuffer -> {
                        // 读取响应体内容
                        byte[] content = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(content);
                        // 只记录前 1024 字节的响应体
                        String responseBody = new String(content, StandardCharsets.UTF_8);
                        if (responseBody.length() > 1024) {
                            log.info("响应内容（前 1024 字节）：{}", responseBody.substring(0, 1024));
                        } else {
                            log.info("响应内容：{}", responseBody);
                        }
                        // 记录响应时间
                        long duration = System.currentTimeMillis() - startTime;
                        log.info("请求路径: {}, 响应时间: {} ms", requestPath, duration);

                        return bufferFactory.wrap(content);
                    }));
                }
                return super.writeWith(body);
            }
        };

        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }

    @Override
    public int getOrder() {
        return -10;
    }
}
