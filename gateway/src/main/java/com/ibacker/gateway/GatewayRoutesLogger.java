package com.ibacker.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Slf4j
@Component
public class GatewayRoutesLogger {

    // RouteLocator 获取的是所有动态生效的路由信息，包括通过 RouteLocatorBuilder 添加的动态路由。
    private final RouteLocator routeLocator;

    public GatewayRoutesLogger(RouteLocator routeLocator) {
        this.routeLocator = routeLocator;
    }

    @EventListener(ContextRefreshedEvent.class) // 在 Spring 容器刷新时触发
    public void logRoutes() {
        Flux<Route> routes = routeLocator.getRoutes();
        routes.subscribe(route -> log.info("路由ID: {}, 路径: {}, 目标URI: {}",
                route.getId(),
                route.getPredicate().toString(),
                route.getUri()));
    }
}
