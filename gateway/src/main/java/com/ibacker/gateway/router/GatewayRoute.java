package com.ibacker.gateway.router;

import lombok.Data;

@Data
public class GatewayRoute {
    private Integer id;
    private String path;        // 路由匹配路径
    private String targetUri;   // 转发目标服务地址
    private Boolean enabled;    // 是否启用路由
}
