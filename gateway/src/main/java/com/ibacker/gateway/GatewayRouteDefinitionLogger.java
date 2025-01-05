package com.ibacker.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

@Slf4j
//@Component
public class GatewayRouteDefinitionLogger {

    //RouteDefinitionLocator 仅获取静态定义的路由（如配置文件中的路由）。
    private final RouteDefinitionLocator routeDefinitionLocator;

    public GatewayRouteDefinitionLogger(RouteDefinitionLocator routeDefinitionLocator) {
        this.routeDefinitionLocator = routeDefinitionLocator;
    }

    @EventListener(ContextRefreshedEvent.class) // 在 Spring 容器刷新时触发
    public void logRouteDefinitions() {
        routeDefinitionLocator.getRouteDefinitions()
                .subscribe(routeDefinition -> log.info("路由定义ID: {}, 路径: {}, 目标URI: {}", 
                        routeDefinition.getId(), 
                        routeDefinition.getPredicates(), 
                        routeDefinition.getUri()));
    }
}
