package com.ibacker.gateway.router;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GatewayRouteMapper {
    @Select("SELECT * FROM gateway_routes WHERE enabled = true")
    List<GatewayRoute> findAllEnabledRoutes();
}
