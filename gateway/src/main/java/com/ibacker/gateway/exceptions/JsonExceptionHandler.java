package com.ibacker.gateway.exceptions;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义异常处理
 * 解决问题：
 * 1 统一网关响应消息格式"{\"status\":" + httpStatus + ",\"message\": \"" + message + "\"}"
 * 2 特殊异常处理，不让其直接抛往前端，比如feign调用过程，某些服务不可用“syscall:getsockopt(..) failed: 拒绝连接: /******:11340”
 *
 * @author tums
 * @date 2018/12/3 21:04
 */
@Slf4j
@Component
public class JsonExceptionHandler implements ErrorWebExceptionHandler {
    /**
     * MessageReader
     */
    private List<HttpMessageReader<?>> messageReaders = Collections.emptyList();
 
    /**
     * MessageWriter
     */
    private List<HttpMessageWriter<?>> messageWriters = Collections.emptyList();
 
    /**
     * ViewResolvers
     */
    private List<ViewResolver> viewResolvers = Collections.emptyList();
 
    /**
     * 存储处理异常后的信息
     */
    private ThreadLocal<Map<String, Object>> exceptionHandlerResult = new ThreadLocal<>();
 
    /**
     * 参考AbstractErrorWebExceptionHandler
     */
    public void setMessageReaders(List<HttpMessageReader<?>> messageReaders) {
        Assert.notNull(messageReaders, "'messageReaders' must not be null");
        this.messageReaders = messageReaders;
    }
 
    /**
     * 参考AbstractErrorWebExceptionHandler
     */
    public void setViewResolvers(List<ViewResolver> viewResolvers) {
        this.viewResolvers = viewResolvers;
    }
 
    /**
     * 参考AbstractErrorWebExceptionHandler
     */
    public void setMessageWriters(List<HttpMessageWriter<?>> messageWriters) {
        Assert.notNull(messageWriters, "'messageWriters' must not be null");
        this.messageWriters = messageWriters;
    }
 
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        // 按照异常类型进行处理
        HttpStatus httpStatus;
        String message;
        if (ex instanceof NotFoundException) {
            httpStatus = HttpStatus.NOT_FOUND;
            message = "Service Not Found";
        }else if (ex instanceof ResponseStatusException) {
            ResponseStatusException responseStatusException = (ResponseStatusException) ex;
            httpStatus = responseStatusException.getStatus();
            message = responseStatusException.getMessage();
        } else {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            message = ObjectUtil.isEmpty(ex.getMessage()) ? "Internal Server Error" : ex.getMessage();
        }
        //封装响应体,此body可修改为自己的jsonBody
        Map<String, Object> result = new HashMap<>(2, 1);
        result.put("httpStatus", httpStatus);
        String msg = "{\"status\":" + httpStatus + ",\"message\": \"" + message + "\"}";
        result.put("body", msg);
        if (exchange.getResponse().isCommitted()) {
            return Mono.error(ex);
        }
        exceptionHandlerResult.set(result);
        ServerRequest newRequest = ServerRequest.create(exchange, this.messageReaders);
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse).route(newRequest)
                .switchIfEmpty(Mono.error(ex))
                .flatMap((handler) -> handler.handle(newRequest))
                .flatMap((response) -> write(exchange, response));
 
    }
 
    /**
     * 参考DefaultErrorWebExceptionHandler
     * <p>
     * 屏蔽特殊异常：{"status":500,"message": "syscall:getsockopt(..) failed: 拒绝连接: /******:11340"}
     */
    private final static String GET_SOCKOPT_FAILED = "syscall:getsockopt(..) failed";
 
    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Map<String, Object> result = exceptionHandlerResult.get();
        HttpStatus httpStatus = (HttpStatus) result.get("httpStatus");
        String body = (String) result.get("body");
        if (httpStatus == HttpStatus.INTERNAL_SERVER_ERROR) {
            log.error("path = {},body = {}", request.path(), body);
            if (body.contains(GET_SOCKOPT_FAILED)) {
                body = "{\"status\":" + httpStatus + ",\"message\": \"系统繁忙，请稍后重试\"}";
            }
        }
        return ServerResponse.status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(body));
    }
 
    /**
     * 参考AbstractErrorWebExceptionHandler
     */
    private Mono<? extends Void> write(ServerWebExchange exchange,
                                       ServerResponse response) {
        exchange.getResponse().getHeaders()
                .setContentType(response.headers().getContentType());
        return response.writeTo(exchange, new ResponseContext());
    }
 
    /**
     * 参考AbstractErrorWebExceptionHandler
     */
    private class ResponseContext implements ServerResponse.Context {
 
        @Override
        public List<HttpMessageWriter<?>> messageWriters() {
            return JsonExceptionHandler.this.messageWriters;
        }
 
        @Override
        public List<ViewResolver> viewResolvers() {
            return JsonExceptionHandler.this.viewResolvers;
        }
 
    }
 
}