package com.atguigu.springcloud.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GateWayConfig {
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder){
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
//        相当于:
//            routes:
//            - id: payment_routh #payment_routh   就是id
//            uri: http://localhost:8001  就是uri
//            predicates:
//            - Path=/payment/get/**     就是/guonei
        routes.route("path_route_guonei",
                r -> r.path("/guonei").uri("http://news.baidu.com/guonei")
        ).build();
        return routes.build();
    }
    @Bean
    public RouteLocator routeLocator2(RouteLocatorBuilder routeLocatorBuilder){
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
        routes.route("path_route_guoji",
                r -> r.path("/guoji").uri("http://news.baidu.com/guoji")
        ).build();
        return routes.build();
    }
}
