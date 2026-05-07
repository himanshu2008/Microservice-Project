package com.microservice.api_gateway.routes;

import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.web.servlet.function.RequestPredicates.path;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class Routes {
    
    @Bean
    public RouterFunction<ServerResponse> productServiceRoute() {
        return route("product_service")
            .route(path("/api/product"), http())
            .before(uri("http://localhost:8080"))
            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> orderServiceRoute() {
        return route("order_service")
            .route(path("/api/order"), http())
            .before(uri("http://localhost:8081"))
            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> inventoryServiceRoute() {
        return route("inventory_service")
            .route(path("/api/inventory"), http())
            .before(uri("http://localhost:8082"))
            .build();
    }
}
