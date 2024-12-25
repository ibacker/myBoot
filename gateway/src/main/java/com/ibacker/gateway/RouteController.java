package com.ibacker.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/route")
public class RouteController {

    private final DynamicRouteService dynamicRouteService;

    @Autowired
    public RouteController(DynamicRouteService dynamicRouteService) {
        this.dynamicRouteService = dynamicRouteService;
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refreshRoutes() {
        dynamicRouteService.updateRoutes();
        return ResponseEntity.ok("Routes refreshed successfully!");
    }
}
