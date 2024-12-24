package com.ibacker.gateway;

import com.ibacker.gateway.router.GatewayRoute;
import com.ibacker.gateway.router.GatewayRouteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.Collections;
import java.util.List;
@Service
public class DynamicRouteService {

    private final GatewayRouteMapper routeMapper;
    private final RouteDefinitionWriter routeDefinitionWriter;
    private final ApplicationEventPublisher publisher;

    @Autowired
    public DynamicRouteService(GatewayRouteMapper routeMapper,
                               RouteDefinitionWriter routeDefinitionWriter,
                               ApplicationEventPublisher publisher) {
        this.routeMapper = routeMapper;
        this.routeDefinitionWriter = routeDefinitionWriter;
        this.publisher = publisher;
    }

    /**
     * 在应用启动时加载数据库路由
     */
    @PostConstruct
    public void loadRoutes() {
        try {
            // 从数据库中加载路由
            List<GatewayRoute> routes = routeMapper.findAllEnabledRoutes();

            for (GatewayRoute route : routes) {
                // 构建 RouteDefinition
                RouteDefinition definition = new RouteDefinition();
                definition.setId(String.valueOf(route.getId())); // 设置路由ID
                definition.setUri(URI.create(route.getTargetUri())); // 设置目标服务地址

                // 创建 Path 断言
                PredicateDefinition predicate = new PredicateDefinition();
                predicate.setName("Path");
                predicate.addArg("pattern", route.getPath()); // 参数名为 "pattern"

                definition.setPredicates(Collections.singletonList(predicate)); // 添加断言

                // 保存路由定义
                routeDefinitionWriter.save(Mono.just(definition)).subscribe();
            }

            // 通知网关刷新路由
            publisher.publishEvent(new RefreshRoutesEvent(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 动态更新路由
     */
    public void updateRoutes() {
        loadRoutes();
    }
}
