package com.ibacker.gateway;

import com.ibacker.gateway.router.GatewayRoute;
import com.ibacker.gateway.router.GatewayRouteMapper;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class DatabaseRouteDefinitionLocator implements RouteDefinitionLocator {

    private final GatewayRouteMapper routeMapper;

    public DatabaseRouteDefinitionLocator(GatewayRouteMapper routeMapper) {
        this.routeMapper = routeMapper;
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        List<GatewayRoute> routes = routeMapper.findAllEnabledRoutes();
        List<RouteDefinition> routeDefinitions = new ArrayList<>();

        for (GatewayRoute route : routes) {
            RouteDefinition definition = new RouteDefinition();
            definition.setId(route.getId().toString());
            definition.setUri(URI.create(route.getTargetUri()));

            // 设置 Path 断言
            PredicateDefinition predicate = new PredicateDefinition();
            predicate.setName("Path");
            predicate.addArg("pattern", route.getPath()); // 使用固定参数名 "pattern"
            definition.setPredicates(Collections.singletonList(predicate));

            // 动态添加 RewritePath 过滤器以移除 path 前缀
            String pathPrefix = route.getPath().replace("/**", ""); // 获取前缀
            if (!pathPrefix.isEmpty()) {
                FilterDefinition rewriteFilter = new FilterDefinition();
                rewriteFilter.setName("RewritePath");
                rewriteFilter.addArg("regexp", "^" + pathPrefix + "/(?<segment>.*)"); // 动态正则匹配
                rewriteFilter.addArg("replacement", "/${segment}"); // 替换为剩余路径
                definition.setFilters(Collections.singletonList(rewriteFilter));
            }


            routeDefinitions.add(definition);
        }

        return Flux.fromIterable(routeDefinitions);
    }
}
