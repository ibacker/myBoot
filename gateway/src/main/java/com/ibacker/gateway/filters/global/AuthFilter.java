package com.ibacker.gateway.filters.global;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ibacker.gateway.adapter.ResultObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @author lei
 * @create 2022-04-07 17:19
 * @desc 权限过滤器
 **/
@Slf4j
//@Component
public class AuthFilter implements GlobalFilter, Ordered {


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("Executing AuthFilter - Pre");

        String path = exchange.getRequest().getURI().getPath();

        String token = exchange.getRequest().getHeaders().getFirst("token");
        //按业务验证对比token
        if (token == null || token.replace(" ", "").isEmpty()) {
            log.error("token is null");
            byte[] bytes = com.alibaba.fastjson.JSON.toJSONString(ResultObject.error(401,"not authorized"), SerializerFeature.WriteMapNullValue)
                    .getBytes(StandardCharsets.UTF_8);
            ServerHttpResponse response = exchange.getResponse();
            response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
            DataBuffer buffer = response.bufferFactory().wrap(bytes);
            //响应出去
//            return response.writeWith(Flux.just(buffer));

        }
        log.info("{}=={}", exchange.getRequest().getMethod(), exchange.getRequest().getURI());
        //当前放行,交由下个过滤器过滤
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            System.out.println("Executing AuthFilter - Post");
        }));
    }

    @Override
    public int getOrder() {
        return -2;
    }
}
