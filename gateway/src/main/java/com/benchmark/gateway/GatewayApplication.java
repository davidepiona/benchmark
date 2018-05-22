package com.benchmark.gateway;


import com.netflix.ribbon.proxy.annotation.Hystrix;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@EnableCircuitBreaker
@SpringBootApplication
public class GatewayApplication {


    @Bean
    DiscoveryClientRouteDefinitionLocator discoveryRoutes(DiscoveryClient dc) {
        return new DiscoveryClientRouteDefinitionLocator(dc);
    }

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);

    }

}

/* TODO: 22/05/18 ELENCO PROBLEMI DEL GATEWAY:
   - il timeout è troppo breve e non riesco a differenziarlo tra le varie richieste
   - se il servizio richiesto non è UP, solleva internamente NotFoundException sul servizio in questione, ma ritorna 'null' quindi è difficile capire che problema ha avuto
*/