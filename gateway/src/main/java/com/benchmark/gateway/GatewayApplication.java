package com.benchmark.gateway;


import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.converters.Auto;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.ribbon.proxy.annotation.Hystrix;
import javafx.application.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.route.builder.UriSpec;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@EnableCircuitBreaker
@SpringBootApplication
public class GatewayApplication {

    @Autowired
    private PathProperties props;


    @Bean DiscoveryClientRouteDefinitionLocator discoveryRoutes (DiscoveryClient dc) {
        return new DiscoveryClientRouteDefinitionLocator(dc);
    }

//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route("bin", r -> r.path("/get")
//                        .filters(f ->
//                                f.addResponseHeader("X-TestHeader", "foobar"))
//                        .uri("http://httpbin.org:80")
//                )
//                .route("upload", r -> r.path("/api/upload/**")
//                        .filters(f -> f.rewritePath("/api/upload/(?<segment>.*)", "api/upload/${segment}"))
//                        .uri("lb://upload-service/api/upload")
//                )
////                .route("registry", r -> r.path("/api/**")
////                        .filters(f -> f.rewritePath("/api/(?<segment>.*)", "api/${segment}"))
////                        //  .filters()
////                        .uri("lb://registry-service")
////                )
//                .route("stream", r -> r.path("/stream/**")
//                        .filters(f -> f.rewritePath("/stream/(?<segment>.*)", "stream/${segment}"))
//                        .uri(props.getNginx())
//                )
//                .route(r -> r.path("/**")
////                        .filters(f -> {
////                            f.rewritePath("/(?<segment>.*)", "/${segment}");
////                            return f.stripPrefix(0);
////                        })
//                        //.uri("http://localhost:3000")
//                        .uri(props.getGui())
//                )
//                .build();
//    }


    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);


    }

}
